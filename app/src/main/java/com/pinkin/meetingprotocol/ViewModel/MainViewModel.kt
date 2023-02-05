package com.pinkin.meetingprotocol.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinkin.meetingprotocol.MainState
import java.util.*

const val TAG = "ViewModel"



class MainViewModel(): ViewModel() {

    private val stateLiveMutable = MutableLiveData<MainState>()
    val stateLive: LiveData<MainState> = stateLiveMutable

    init {
        Log.e(TAG, "VM created")
        stateLiveMutable.value = MainState("Roman", Calendar.getInstance().time, "Time", "Protocol")
    }

    fun updateDate(newDate: Date) {
        stateLiveMutable.postValue(
            stateLiveMutable.value?.let {
                MainState(
                    name = it.name,
                    date = newDate,
                    time = it.time,
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