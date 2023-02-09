package com.pinkin.businesslogic.UseCase

import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.businesslogic.Repository.RoomRepository

class GetProtocolsUseCase(private val repository: RoomRepository) {


    fun execute(): List<Protocol> {

        return repository.getProtocols()
    }
}