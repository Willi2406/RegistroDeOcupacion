package com.example.registrodeocupacion.domain.empleado.usecase

import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import jakarta.inject.Inject

class DeleteEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}
