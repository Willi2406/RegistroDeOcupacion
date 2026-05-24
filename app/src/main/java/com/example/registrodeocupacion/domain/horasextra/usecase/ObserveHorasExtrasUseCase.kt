package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import com.example.registrodeocupacion.domain.horasextra.repository.HorasExtrasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHorasExtrasUseCase @Inject constructor(
    private val repository: HorasExtrasRepository
) {
    operator fun invoke(): Flow<List<HoraExtra>> = repository.observeHorasExtras()
}