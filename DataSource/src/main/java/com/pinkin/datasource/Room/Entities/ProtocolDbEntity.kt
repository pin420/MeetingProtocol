package com.pinkin.datasource.Room.Entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "protocol",
)
data class ProtocolDbEntity(@ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int) {


}

