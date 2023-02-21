package com.pinkin.meetingprotocol

import android.content.Context
import android.preference.PreferenceManager

object SharedPreferences {


    fun getPrefLearn(context: Context, key: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(key, false)
    }

    fun setPrefLearnTrue(context: Context, key: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(key, true)
            .apply()
    }

    fun setPrefLearnFalse(context: Context, key: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(key, false)
            .apply()
    }

}