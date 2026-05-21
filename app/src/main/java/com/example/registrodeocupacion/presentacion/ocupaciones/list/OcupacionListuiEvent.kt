package com.example.registrodeocupacion.presentacion.ocupaciones.list

import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion

data class OcupacionListUiState(
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)