package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import com.example.registrodeocupacion.domain.horasextra.repository.HorasExtrasRepository
import javax.inject.Inject

class UpsertHorasExtrasUseCase @Inject constructor(
    private val repository: HorasExtrasRepository
) {
    suspend operator fun invoke(horasExtras: HoraExtra): Result<Int> {

        val fechaResult = validarFecha(horasExtras.fecha)
        if (!fechaResult.isValid) {
            return Result.failure(exception = IllegalArgumentException(fechaResult.error))
        }

        val cantidadResult = validarCantidadHora(horasExtras.cantidadHoras.toString())
        if (!cantidadResult.isValid) {
            return Result.failure(exception = IllegalArgumentException(cantidadResult.error))
        }

        val tipoResult = validarTipo(horasExtras.tipo.toString())
        if (!tipoResult.isValid) {
            return Result.failure(exception = IllegalArgumentException(tipoResult.error))
        }

        val empleadoResult = validarEmpleado(horasExtras.empleadoId.toString())
        if (!empleadoResult.isValid) {
            return Result.failure(exception = IllegalArgumentException(empleadoResult.error))
        }

        return runCatching { repository.upsert(horasExtras) }
    }
}