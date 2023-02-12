package com.pinkin.datasource.Room.Repository

import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.businesslogic.Repository.RoomRepository
import com.pinkin.datasource.Room.Dao.ProtocolsDao
import com.pinkin.datasource.Room.Entities.ProtocolDbEntity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class RoomRepositoryRealization(
    private val protocolsDao: ProtocolsDao
) : RoomRepository {

    @OptIn(DelicateCoroutinesApi::class)
    override fun saveProtocol(id: Int, name: String, dateTime: Date, protocol: String) {

        val entity = ProtocolDbEntity.fromProtocolDbEntity(
            id,
            name,
            SimpleDateFormat("dd.MM.yyyy").format(dateTime),
            SimpleDateFormat("HH:mm").format(dateTime),
            protocol)

        GlobalScope.launch {
            protocolsDao.setProtocol(entity)
        }
    }

    override fun getProtocols(): List<Protocol> {

        val protocolsDB = protocolsDao.giveProtocols()
        val protocols: MutableList<Protocol> = mutableListOf()

        for (protocol in protocolsDB) {
            protocols.add(protocol.toProtocol())
        }

        return protocols
    }

    override fun deleteProtocol(id: Int) {

        protocolsDao.deleteProtocol(id)

    }

    override fun getSearchProtocols(query: String, searchOption: Int): List<Protocol> {

        val protocolsDB = when (searchOption) {
            0 -> protocolsDao.getSearchByName(query)
            1 -> protocolsDao.getSearchByProtocol(query)
            2 -> protocolsDao.getSearch(query)
            else -> {
                protocolsDao.giveProtocols()
            }
        }

        val protocols: MutableList<Protocol> = mutableListOf()

        for (protocol in protocolsDB) {
            protocols.add(protocol.toProtocol())
        }

        return protocols


    }
}









