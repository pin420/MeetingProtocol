package com.pinkin.meetingprotocol

interface MainEvent

class DropDatabaseEvent : MainEvent

class GetProtocolsEvent : MainEvent

class GetSearchProtocolsEvent : MainEvent {

    private var query: String = ""

    fun setQuery(data: String){
        query = data
    }

    fun getQuery(): String {
        return query
    }

}

class DeleteProtocolEvent : MainEvent {

    private var id: Int = 0

    fun setId(data: Int) {
        id = data
    }

    fun getId(): Int {
        return id
    }
}

class SaveProtocolEvent : MainEvent {

    private var id: Int = 0
    private var name: String = ""
    private var protocol: String = ""

    fun setId(data: Int) {
        id = data
    }

    fun getId(): Int {
        return id
    }

    fun setName(data: String) {
        name = data
    }

    fun getName(): String {
        return name
    }

    fun setProtocol(data: String) {
        protocol = data
    }

    fun getProtocol(): String {
        return protocol
    }
}

