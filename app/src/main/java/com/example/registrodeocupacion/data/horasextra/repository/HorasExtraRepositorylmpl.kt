package com.example.registrodeocupacion.data.horasextra.repository

import com.example.registrodeocupacion.data.horasextra.local.HorasExtraDao
import com.example.registrodeocupacion.data.horasextra.mapper.toDomain
import com.example.registrodeocupacion.data.horasextra.mapper.toEntity
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import com.example.registrodeocupacion.domain.horasextra.repository.HorasExtrasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.TimeSource

class HorasExtraRepositorylmpl @Inject constructor(
    private val localDataSource: HorasExtraDao
): HorasExtrasRepository{
    override fun observeHorasExtras(): Flow<List<HoraExtra>>{
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHorasExtra(id: Int): HoraExtra? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(horaExtra: HoraExtra): Int {
        localDataSource.upsert(entity = horaExtra.toEntity())
        return horaExtra.horasExtraId
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return localDataSource.exists(id)
    }

}

