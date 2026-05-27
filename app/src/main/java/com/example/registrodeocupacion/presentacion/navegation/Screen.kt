package com.example.registrodeocupacion.presentacion.navegation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object EmpleadoList : Screen()

    @Serializable
    data class EmpleadoForm(val empleadoId: Int) : Screen()

    @Serializable
    data object OcupacionList : Screen()

    @Serializable
    data class OcupacionForm(val ocupacionId: Int) : Screen()


    @Serializable
    data object HorasExtrasList : Screen()

    @Serializable
    data class HorasExtrasForm(val horasExtraId: Int) : Screen()
}