package com.pinkin.businesslogic.UseCase

import com.pinkin.businesslogic.Repository.RoomRepository
import java.util.*

class SaveProtocolUseCase(private val repository: RoomRepository) {

    fun execute(name: String, dateTime: Date, protocol: String) {

        repository.saveProtocol(name, dateTime, protocol)
    }



}