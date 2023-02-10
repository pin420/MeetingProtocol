package com.pinkin.datasource.Room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pinkin.datasource.Room.Entities.ProtocolDbEntity


@Dao
interface ProtocolsDao {

    @Insert(entity = ProtocolDbEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun setProtocol(protocolDbEntity: ProtocolDbEntity)

    @Query("SELECT * FROM protocols")
    fun giveProtocols(): List<ProtocolDbEntity>
}