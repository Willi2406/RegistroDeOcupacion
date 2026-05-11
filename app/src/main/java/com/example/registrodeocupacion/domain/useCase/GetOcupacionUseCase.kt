package com.example.registrodeocupacion.domain.useCase

import com.example.registrodeocupacion.domain.model.Ocupacion
import com.example.registrodeocupacion.domain.repository.OcupacionRepository
import javax.inject.Inject
class GetOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository){
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOcupacion(id)
}