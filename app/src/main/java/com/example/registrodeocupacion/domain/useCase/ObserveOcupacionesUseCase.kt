package com.example.registrodeocupacion.domain.useCase

import com.example.registrodeocupacion.domain.model.Ocupacion
import com.example.registrodeocupacion.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOcupacionesUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> = repository.observeOcupaciones()
}
