package com.example.registrodeocupacion.domain.ocupacion.usecase

import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import javax.inject.Inject

class DeleteOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}