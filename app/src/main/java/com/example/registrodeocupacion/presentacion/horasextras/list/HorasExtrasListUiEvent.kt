package com.example.registrodeocupacion.presentacion.horasextras.list

sealed interface HorasExtrasListUiEvent {
    object Load : HorasExtrasListUiEvent
    object Refresh : HorasExtrasListUiEvent
    data class ShowMessage(val message: String) : HorasExtrasListUiEvent
    object ClearMessage : HorasExtrasListUiEvent
    object CreateNew : HorasExtrasListUiEvent
    data class Edit(val id: Int) : HorasExtrasListUiEvent
}