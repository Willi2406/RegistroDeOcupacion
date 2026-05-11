package com.example.registrodeocupacion.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Upsert
    suspend fun upsert(entity: OcupacioneEntity)
    @Delete
    suspend fun delete(entity: OcupacioneEntity)
    @Query("Select * from ocupaciones")
    fun observeAll(): Flow<List<OcupacioneEntity>>
    @Query("Select * from ocupaciones Where ocupacioneId =:id")
    suspend fun getById(id: Int): OcupacioneEntity?
    @Query("Delete from ocupaciones Where ocupacioneId =:id")
    suspend fun deleteById(id: Int)
    @Query("select exists (select 1 from Ocupaciones Where ocupacioneId =:id)")
    suspend fun exists(id: Int): Boolean
}