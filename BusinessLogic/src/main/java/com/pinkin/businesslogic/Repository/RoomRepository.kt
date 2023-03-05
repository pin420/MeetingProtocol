package com.pinkin.businesslogic.Repository

import com.pinkin.businesslogic.Model.Protocol
import java.util.*

interface RoomRepository {

    suspend fun saveProtocol(id: Int, name: String, dateTime: Date, protocol: String)

    fun getProtocols(): List<Protocol>

    fun deleteProtocol(id: Int)

    fun getSearchProtocols(query: String, searchOption: Int): List<Protocol>

    fun dropDatabase()

}