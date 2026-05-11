package com.example.registrodeocupacion.domain.useCase

import com.example.registrodeocupacion.domain.model.Ocupacion
import com.example.registrodeocupacion.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow

class ObserveOcupacionesUseCase (
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}