package com.example.registrodeocupacion.data.empleado.repository

import com.example.registrodeocupacion.data.empleado.local.EmpleadoDao
import com.example.registrodeocupacion.data.empleado.mapper.toDomain
import com.example.registrodeocupacion.data.empleado.mapper.toEntity
import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(
    private val localDataSource: EmpleadoDao
): EmpleadoRepository{
    override fun observeEmpleados(): Flow<List<Empleado>> {
        return localDataSource.observeAll().map{entities ->
            entities.map {it.toDomain()}
        }
    }

    override suspend fun getEmpleado(id: Int): Empleado?{
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int{
        localDataSource.upsert(entity = empleado.toEntity())
        return empleado.empleadoId
    }

    override suspend fun delete(id: Int){
        localDataSource.deleteById(id)
    }

    override suspend fun exists(Id: Int): Boolean {
        return localDataSource.exists(Id)
    }
}