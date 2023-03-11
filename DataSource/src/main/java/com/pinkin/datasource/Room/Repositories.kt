package com.pinkin.datasource.Room

import android.content.Context
import androidx.room.Room
import com.pinkin.businesslogic.Repository.RoomRepository
import com.pinkin.datasource.Room.Repository.RoomRepositoryRealization

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }


    // --- repositories

    val roomRepository: RoomRepository by lazy {
        RoomRepositoryRealization(database.getProtocolsDao())
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}