package com.example.registrodeocupacion.presentacion.empleado.list

import com.example.registrodeocupacion.domain.empleado.model.Empleado

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)