package com.example.registrodeocupacion.data.empleado.local

import android.R

enum class FrecuenciaPago(
    val dias: Double,
    val descripcion: String
) {
    SEMANAL(5.5, "Semanal"),
    QUINCENAL(11.91, "Quincenal"),
    MENSUAL(23.83, "Mensual")
}
