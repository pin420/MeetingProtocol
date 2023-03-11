package com.pinkin.datasource.Room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pinkin.businesslogic.Model.Protocol
import java.text.SimpleDateFormat

@Entity(tableName = "protocols")
data class ProtocolDbEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "displayTime") val displayTime: String,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "protocol") val protocol: String
) {

    fun toProtocol() = Protocol(
        id = id,
        name = name,
        date = displayTime.split(" ")[0],
        time = displayTime.split(" ")[1],
        protocol = protocol
    )

    companion object {
        private var format: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        fun fromProtocolDbEntity(id: Int, nameData: String, dateData: String, timeData: String, protocolData: String) =
            format.parse("$dateData $timeData")?.let {
                ProtocolDbEntity(
                    id = id,
                    name = nameData,
                    time = it.time,
                    displayTime = "$dateData $timeData",
                    protocol = protocolData
                )
            }
    }
}

