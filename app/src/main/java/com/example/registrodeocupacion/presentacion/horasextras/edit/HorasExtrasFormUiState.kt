package com.example.registrodeocupacion.presentacion.horasextras.edit

import com.example.registrodeocupacion.data.horasextra.local.TipoHoraExtra
import com.example.registrodeocupacion.domain.empleado.model.Empleado
import java.time.LocalDate

data class HorasExtrasFormUiState(
    val horasExtraId: Int? = null,
    val empleadoId: Int? = null,
    val fecha: LocalDate = LocalDate.now(),
    val cantidadHoras: String = "",
    val tipo: TipoHoraExtra = TipoHoraExtra.DIURNO,
    val recargo: String = "",
    val empleadoIdError: String? = null,
    val fechaError: String? = null,
    val cantidadHorasError: String? = null,
    val recargoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val empleadosDisponibles: List<Empleado> = emptyList()
)