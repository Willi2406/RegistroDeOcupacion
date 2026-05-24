package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.domain.horasextra.repository.HorasExtrasRepository
import javax.inject.Inject

class DeleteHorasExtrasUseCase @Inject constructor(
    private val repository: HorasExtrasRepository
){
    suspend operator fun invoke(id: Int) = repository.delete(id)
}