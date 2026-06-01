package com.example.registrodeocupacion.presentacion.horasextras.edit

import com.example.registrodeocupacion.data.horasextra.local.TipoHoraExtra
import java.time.LocalDate

sealed interface HorasExtrasFormUiEvent {
    data class Load(val id: Int) : HorasExtrasFormUiEvent
    data class EmpleadoIdChanged(val value: Int) : HorasExtrasFormUiEvent
    data class FechaChanged(val value: LocalDate) : HorasExtrasFormUiEvent
    data class CantidadHorasChanged(val value: String) : HorasExtrasFormUiEvent
    data class TipoChanged(val value: TipoHoraExtra) : HorasExtrasFormUiEvent
    data class RecargoChanged(val value: String) : HorasExtrasFormUiEvent
    object Calcular : HorasExtrasFormUiEvent
    object Save : HorasExtrasFormUiEvent
    object Delete : HorasExtrasFormUiEvent
}