package com.example.registrodeocupacion.domain.horasextra.repository

import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import kotlinx.coroutines.flow.Flow

interface HorasExtrasRepository {
    fun observeHoraExtras(): Flow<List<HoraExtra>>

    suspend fun getHorasExtra(id: Int): HoraExtra?

    suspend fun upsert(horaExtra: HoraExtra) : Int

    suspend fun delete(id: Int)

    suspend fun exists(id: Int) : Boolean
}