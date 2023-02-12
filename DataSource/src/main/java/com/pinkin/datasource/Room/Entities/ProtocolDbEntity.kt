package com.pinkin.datasource.Room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pinkin.businesslogic.Model.Protocol

@Entity(tableName = "protocols")
data class ProtocolDbEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "protocol") val protocol: String
) {

    fun toProtocol() = Protocol(
        id = id,
        name = name,
        date = date,
        time = time,
        protocol = protocol
    )

    companion object {
        fun fromProtocolDbEntity(id: Int, nameData: String, dateData: String, timeData: String, protocolData: String) = ProtocolDbEntity(
            id = id, // SQLite generates identifier automatically if ID = 0
            name = nameData,
            date = dateData,
            time = timeData,
            protocol = protocolData
        )
    }
}

