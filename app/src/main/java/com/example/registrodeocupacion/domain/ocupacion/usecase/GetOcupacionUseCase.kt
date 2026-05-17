package com.example.registrodeocupacion.domain.ocupacion.usecase

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import javax.inject.Inject
class GetOcupacionUseCase @Inject constructor(private val repository: OcupacionRepository){
    suspend operator fun invoke(id: Int): Ocupacion? = repository.getOcupacion(id)
}