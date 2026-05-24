package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import com.example.registrodeocupacion.domain.horasextra.repository.HorasExtrasRepository
import javax.inject.Inject

class GetHorasExtrasUseCase @Inject constructor(private val repository: HorasExtrasRepository){
    suspend operator fun invoke(id: Int): HoraExtra? = repository.getHorasExtra(id)
}