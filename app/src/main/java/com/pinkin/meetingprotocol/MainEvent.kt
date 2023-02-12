package com.pinkin.meetingprotocol

interface MainEvent


class GetProtocolsEvent : MainEvent

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

