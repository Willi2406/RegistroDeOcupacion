package com.example.registrodeocupacion.presentacion.ocupaciones.edit

sealed interface OcupacionFormUiEvent {
    data class Load(val id: Int) : OcupacionFormUiEvent
    data class DescripcionChanged(val value: String) : OcupacionFormUiEvent

    data class EsPuestoDireccionChanged(val value: Boolean) : OcupacionFormUiEvent
    data object Save : OcupacionFormUiEvent
    data object Delete : OcupacionFormUiEvent

}