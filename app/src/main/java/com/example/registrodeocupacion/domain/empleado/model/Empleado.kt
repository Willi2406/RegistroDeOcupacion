package com.example.registrodeocupacion.domain.empleado.model

import java.sql.RowId
import java.time.LocalDate

class Empleado(
    val empleadoId: Int = 0,
    val fechaIngreso: LocalDate = LocalDate.now(),
    val nombres: String = "",
    val sexo: String = "",
    val sueldo: Double = 0.0,

)