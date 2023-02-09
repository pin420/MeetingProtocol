package com.pinkin.meetingprotocol

interface MainEvent

class GetProtocolsEvent : MainEvent

class SaveProtocolEvent : MainEvent {

    private var name: String = ""
    private var protocol: String = ""

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

