package com.pinkin.businesslogic.UseCase

import com.pinkin.businesslogic.Repository.RoomRepository

class DeleteProtocolUseCase(private val repository: RoomRepository) {

    fun execute(id: Int) {

        repository.deleteProtocol(id)
    }
}