package com.example.registrodeocupacion.domain.empleado.usecase

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.validateDescription
import com.example.registrodeocupacion.domain.ocupacion.usecase.validateSueldo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(empleado: Empleado): Result<Int>{
        val nombresResult = validarNombres(empleado.nombres)

        if(!nombresResult.isValid){
            return Result.failure(exception = IllegalArgumentException(nombresResult.error))
        }

        val sueldoResult = validateSueldo(empleado.sueldo.toString())
        if(!sueldoResult.isValid){
            return Result.failure(exception = IllegalArgumentException(sueldoResult.error))
        }

        val sexoResult = validarSexo(empleado.sexo)
        if(!sexoResult.isValid){
            return Result.failure(exception = IllegalArgumentException(sexoResult.error))
        }

        val fechaResult = validarFecha(empleado.fechaIngreso)
        if(!fechaResult.isValid){
            return Result.failure(exception = IllegalArgumentException(fechaResult.error))
        }
        return runCatching { repository.upsert(empleado) }
    }
}