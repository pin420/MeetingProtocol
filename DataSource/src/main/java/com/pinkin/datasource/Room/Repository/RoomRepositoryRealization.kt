package com.pinkin.datasource.Room.Repository

import com.pinkin.businesslogic.Repository.RoomRepository
import com.pinkin.datasource.Room.Dao.ProtocolsDao
import com.pinkin.datasource.Room.Entities.ProtocolDbEntity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class RoomRepositoryRealization(
    private val protocolsDao: ProtocolsDao,
    private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {

    @OptIn(DelicateCoroutinesApi::class)
    override fun saveProtocol(name: String, dateTime: Date, protocol: String) {

        val entity = ProtocolDbEntity.fromProtocolDbEntity(
            name,
            SimpleDateFormat("dd.MM.yyyy").format(dateTime),
            SimpleDateFormat("HH:mm").format(dateTime),
            protocol)

        GlobalScope.launch {
            protocolsDao.setProtocol(entity)
        }
    }
}









