package com.pinkin.datasource.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pinkin.datasource.Room.Dao.ProtocolsDao
import com.pinkin.datasource.Room.Entities.ProtocolDbEntity

@Database(
    version = 1,
    entities = [
        ProtocolDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProtocolsDao(): ProtocolsDao
}