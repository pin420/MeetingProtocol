package com.pinkin.meetingprotocol.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinkin.meetingprotocol.MainState

const val TAG = "ViewModel"

class MainViewModel(

    ): ViewModel() {

    private val stateLiveMutable = MutableLiveData<MainState>()
    val stateLive: LiveData<MainState> = stateLiveMutable

    init {
        Log.e(TAG, "VM created")

        stateLiveMutable.value = MainState("Roman", "Data", "Time", "Protocol")
6
    }

    override fun onCleared() {
        Log.e(TAG, "VM cleared")
        super.onCleared()
    }

}