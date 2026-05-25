package com.example.registrodeocupacion.presentacion.empleado.edit


import com.example.registrodeocupacion.data.empleado.local.FrecuenciaPago
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import java.time.LocalDate

data class EmpleadoFormUiState (
    val empleadoId: Int? = null,
    val fechaIngreso: LocalDate = LocalDate.now(),
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: String = "",
    val fechaIngresoError: String? = null,
    val nombresError: String? = null,
    val sexoError: String? = null,
    val sueldoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val frecuenciaPago: FrecuenciaPago = FrecuenciaPago.MENSUAL,
    val ocupacionId: Int? = null,
    val frecuenciaPagoError: String? = null,
    val ocupacionIdError: String? = null,
    val ocupacionesDisponibles: List<Ocupacion> = emptyList()
)