package com.example.registrodeocupacion.data.repository

import com.example.registrodeocupacion.data.local.OcupacionDao
import com.example.registrodeocupacion.data.mapper.toDomain
import com.example.registrodeocupacion.data.mapper.toEntity
import com.example.registrodeocupacion.domain.model.Ocupacion
import com.example.registrodeocupacion.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(
    private val localDataSource: OcupacionDao
): OcupacionRepository{
    override fun observeOcupaciones(): Flow<List<Ocupacion>> {
        return localDataSource.observeAll().map{entities ->
            entities.map { it.toDomain()}
        }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        localDataSource.upsert( entity = ocupacion.toEntity())
        return ocupacion.ocupacioneId
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean{
        return localDataSource.exists(id)
    }
}