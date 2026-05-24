package com.example.registrodeocupacion.domain.ocupacion.usecase
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
){
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int>{
        val listaActual = repository.observeOcupaciones().first().map { it.descricion }
        val descriptionResult = validateDescription(ocupacion.descricion, listaActual)

        if(!descriptionResult.isValid){
            return Result.failure(exception = IllegalArgumentException(descriptionResult.error))
        }

        return runCatching { repository.upsert(ocupacion) }
    }
}