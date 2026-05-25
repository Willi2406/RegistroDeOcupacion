package com.example.registrodeocupacion.domain.horasextra.model

import com.example.registrodeocupacion.data.horasextra.local.TipoHoraExtra
import java.time.LocalDate

data class HoraExtra (
    val horasExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras: Double = 0.0,
    val tipo: TipoHoraExtra = TipoHoraExtra.DIURNO,
    val recargo: Double = 0.0
)
