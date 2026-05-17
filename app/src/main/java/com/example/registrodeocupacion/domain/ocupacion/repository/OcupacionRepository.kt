package com.example.registrodeocupacion.domain.ocupacion.repository

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import kotlinx.coroutines.flow.Flow

interface OcupacionRepository {
    fun observeOcupaciones(): Flow<List<Ocupacion>>
    suspend fun getOcupacion(Id: Int) : Ocupacion?
    suspend fun upsert(ocupacion: Ocupacion) : Int
    suspend fun delete(Id: Int)
    suspend fun exists(Id: Int) : Boolean
}