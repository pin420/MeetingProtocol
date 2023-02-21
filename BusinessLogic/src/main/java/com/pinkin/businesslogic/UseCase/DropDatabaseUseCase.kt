package com.pinkin.businesslogic.UseCase

import com.pinkin.businesslogic.Repository.RoomRepository

class DropDatabaseUseCase(private val repository: RoomRepository) {

    fun execute() {

        repository.dropDatabase()
    }
}