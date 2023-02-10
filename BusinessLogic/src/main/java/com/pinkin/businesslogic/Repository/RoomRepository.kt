package com.pinkin.businesslogic.Repository

import com.pinkin.businesslogic.Model.Protocol
import java.util.*


interface RoomRepository {

    fun saveProtocol(name: String, dateTime: Date, protocol: String)

    fun getProtocols(): List<Protocol>
}