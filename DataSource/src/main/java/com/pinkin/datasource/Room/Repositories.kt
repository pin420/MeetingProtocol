package com.pinkin.datasource.Room

import android.content.Context
import androidx.room.Room
import com.pinkin.businesslogic.Repository.RoomRepository
import com.pinkin.datasource.Room.Repository.RoomRepositoryRealization
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    // --- repositories

    val roomRepository: RoomRepository by lazy {
        RoomRepositoryRealization(database.getProtocolsDao(), ioDispatcher)
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}