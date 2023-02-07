package com.pinkin.datasource.Room.Repository

import android.util.Log
import com.pinkin.businesslogic.Repository.RoomRepository
import com.pinkin.datasource.Room.Dao.ProtocolsDao
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*


class RoomRepositoryRealization(
    private val protocolsDao: ProtocolsDao,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {


    override fun saveProtocol(name: String, dateTime: Date, protocol: String) {

        Log.i("RRRR", name)
        Log.i("RRRR", "$dateTime")
        Log.i("RRRR", protocol)

    }

}









