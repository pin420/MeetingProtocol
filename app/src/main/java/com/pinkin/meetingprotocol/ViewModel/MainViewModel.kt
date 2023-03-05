package com.pinkin.meetingprotocol.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.businesslogic.UseCase.*
import com.pinkin.meetingprotocol.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

const val TAG = "ViewModel"

class MainViewModel(
    private val saveProtocolUseCase: SaveProtocolUseCase,
    private val getProtocolsUseCase: GetProtocolsUseCase,
    private val deleteProtocolUseCase: DeleteProtocolUseCase,
    private val getSearchProtocolsUseCase: GetSearchProtocolsUseCase,
    private val dropDatabaseUseCase: DropDatabaseUseCase,
): ViewModel() {

    private val stateLiveMutable = MutableLiveData<MainState>()
    val stateLive: LiveData<MainState> = stateLiveMutable

    private val protocolsLiveMutable = MutableLiveData<List<Protocol>>()
    val protocolsLive: LiveData<List<Protocol>> = protocolsLiveMutable

    private var searchOption = 0
    private var searchQuery: String? = null
    private lateinit var RecyclerViewPoolVM : RecycledViewPool
    private var RecyclerViewPoolInit = false
    var chanceDateOrTime = false
    var lastEventForLoad: MainEvent = GetProtocolsEvent()

    init {
        Log.e(TAG, "VM created")
        stateLiveMutable.value = MainState("", Calendar.getInstance().time, "")
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun send(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is DropDatabaseEvent -> {
                    val scope = GlobalScope.launch {
                        dropDatabaseUseCase.execute() }

                    scope.invokeOnCompletion {
                        send(lastEventForLoad)
                    }
                }
                is SaveProtocolEvent -> {
                    val scope = GlobalScope.launch {
                        saveProtocolUseCase.execute(
                            event.getId(),
                            event.getName(),
                            stateLiveMutable.value!!.dateTime,
                            event.getProtocol()) }

                    scope.invokeOnCompletion {
                        send(lastEventForLoad)
                    }
                }
                is GetProtocolsEvent -> {
                    lastEventForLoad = event
                    GlobalScope.launch {
                        protocolsLiveMutable.postValue(getProtocolsUseCase.execute())
                    }
                }
                is DeleteProtocolEvent -> {
                    val scope = GlobalScope.launch {
                        deleteProtocolUseCase.execute(event.getId()) }

                    scope.invokeOnCompletion {
                        send(lastEventForLoad)
                    }
                }
                is GetSearchProtocolsEvent -> {
                    lastEventForLoad = event
                    GlobalScope.launch {
                        protocolsLiveMutable.postValue(getSearchProtocolsUseCase.execute(event.getQuery(), getSearchOption()))
                    }

                }
            }
        }
    }

    fun updateDate(newDate: Date) {
        stateLiveMutable.postValue(
            stateLiveMutable.value?.let {
                MainState(
                    name = it.name,
                    dateTime = it.dateTime.apply {
                        year = newDate.year
                        month = newDate.month
                        date = newDate.date
                    },
                    protocol = it.protocol
                )
            }
        )
    }

    fun updateTime(newHour: Int, newMinute: Int) {
        stateLiveMutable.postValue(
            stateLiveMutable.value?.let {
                MainState(
                    name = it.name,
                    dateTime = it.dateTime.apply {
                        hours = newHour
                        minutes = newMinute
                    },
                    protocol = it.protocol
                )
            }
        )
    }

    fun updateDateTime() {
        stateLiveMutable.postValue(
            stateLiveMutable.value?.let {
                MainState(
                    name = it.name,
                    dateTime = Calendar.getInstance().time,
                    protocol = it.protocol
                )
            }
        )
    }

    fun setSearchOption(data: Int) {
        searchOption = data
    }

    fun getSearchOption(): Int {
        return searchOption
    }

    fun setSearchQuery(data: String?) {
        searchQuery = data
    }

    fun getSearchQuery(): String? {
        return searchQuery
    }

    fun setRecyclerPool(data: RecycledViewPool) {
        RecyclerViewPoolVM = data
    }

    fun getRecyclerPool(): RecycledViewPool {
        return RecyclerViewPoolVM
    }

    fun setRecyclerViewPoolInit() {
        RecyclerViewPoolInit = true
    }

    fun getRecyclerViewPoolInit(): Boolean {
        return RecyclerViewPoolInit
    }

    override fun onCleared() {
        Log.e(TAG, "VM cleared")
        super.onCleared()
    }
}