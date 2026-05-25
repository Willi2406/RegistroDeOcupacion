package com.example.registrodeocupacion.presentacion.ocupaciones.edit

data class OcupacionFormUiState(
    val ocupacionId: Int? = null,
    val descripcion: String = "",
    val esPuestoDireccion: Boolean = false,
    val descripcionError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)