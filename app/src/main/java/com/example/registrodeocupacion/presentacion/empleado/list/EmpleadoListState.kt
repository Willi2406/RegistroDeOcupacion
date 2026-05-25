package com.example.registrodeocupacion.presentacion.empleado.list

import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)