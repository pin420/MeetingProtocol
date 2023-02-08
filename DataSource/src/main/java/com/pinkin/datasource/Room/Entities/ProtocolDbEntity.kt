package com.pinkin.datasource.Room.Entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "protocols")
data class ProtocolDbEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "protocol") val protocol: String
) {

    companion object {
        fun fromProtocolDbEntity(nameData: String, dateData: String, timeData: String, protocolData: String) = ProtocolDbEntity(
            id = 0, // SQLite generates identifier automatically if ID = 0
            name = nameData,
            date = dateData,
            time = timeData,
            protocol = protocolData
        )
    }
}

