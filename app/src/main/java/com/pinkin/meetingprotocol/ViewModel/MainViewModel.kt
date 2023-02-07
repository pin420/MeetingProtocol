package com.pinkin.meetingprotocol.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkin.businesslogic.UseCase.SaveProtocolUseCase
import com.pinkin.meetingprotocol.MainEvent
import com.pinkin.meetingprotocol.MainState
import com.pinkin.meetingprotocol.SaveProtocolEvent
import kotlinx.coroutines.launch
import java.util.*

const val TAG = "ViewModel"



class MainViewModel(private val saveProtocolUseCase: SaveProtocolUseCase,): ViewModel() {

    private val stateLiveMutable = MutableLiveData<MainState>()
    val stateLive: LiveData<MainState> = stateLiveMutable

    init {
        Log.e(TAG, "VM created")
        stateLiveMutable.value = MainState("Roman", Calendar.getInstance().time, "Protocol")
    }

    fun send(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is SaveProtocolEvent -> {
                        saveProtocolUseCase.execute(
                            event.getName(),
                            stateLiveMutable.value!!.dateTime,
                            event.getProtocol())
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

    override fun onCleared() {
        Log.e(TAG, "VM cleared")
        super.onCleared()
    }

}