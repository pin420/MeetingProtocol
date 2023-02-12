package com.pinkin.businesslogic.UseCase

import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.businesslogic.Repository.RoomRepository

class GetSearchProtocolsUseCase(private val repository: RoomRepository) {


    fun execute(query: String, searchOption: Int): List<Protocol> {

        return repository.getSearchProtocols(query, searchOption)
    }

}