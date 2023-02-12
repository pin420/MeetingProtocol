package com.pinkin.datasource.Room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pinkin.datasource.Room.Entities.ProtocolDbEntity

@Dao
interface ProtocolsDao {

    @Insert(entity = ProtocolDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProtocol(protocolDbEntity: ProtocolDbEntity)

    @Query("SELECT * FROM protocols ORDER BY protocols.date DESC, protocols.time DESC")
    fun giveProtocols(): List<ProtocolDbEntity>

    @Query("DELETE FROM protocols WHERE protocols.id = :id")
    fun deleteProtocol(id: Int)

}