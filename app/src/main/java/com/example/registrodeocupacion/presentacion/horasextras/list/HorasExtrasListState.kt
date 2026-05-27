package com.example.registrodeocupacion.presentacion.horasextras.list

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra

data class HorasExtrasListUiState(
    val isLoading: Boolean = false,
    val horasExtras: List<HoraExtra> = emptyList(),
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)