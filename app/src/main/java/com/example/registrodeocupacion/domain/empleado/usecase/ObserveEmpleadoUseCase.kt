package com.example.registrodeocupacion.domain.empleado.usecase

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
){
    operator fun invoke(): Flow<List<Empleado>> = repository.observeEmpleados()
}