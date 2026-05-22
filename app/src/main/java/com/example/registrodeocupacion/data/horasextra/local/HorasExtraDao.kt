package com.example.registrodeocupacion.data.horasextra.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HorasExtraDao {
    @Upsert
    suspend fun upsert(entity: HorasExtraEntity)
    @Delete
    suspend fun delete(entity: HorasExtraEntity)
    @Query(value = "Select * from horasextra")
    fun observeAll(): Flow<List<HorasExtraEntity>>
    @Query(value = "Select * from horasextra Where horasExtraId =:id")
    suspend fun getById(id: Int): HorasExtraEntity?
    @Query(value = "Delete from horasextra Where horasExtraId =:id")
    suspend fun deleteById(id: Int)
    @Query(value = "Select exists (select 1 from horasextra Where horasExtraId =:id)")
    suspend fun exists(id: Int): Boolean
}