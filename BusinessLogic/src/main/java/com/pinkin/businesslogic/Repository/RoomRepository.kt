package com.pinkin.businesslogic.Repository

import java.util.*


interface RoomRepository {

    fun saveProtocol(name: String, dateTime: Date, protocol: String)

}