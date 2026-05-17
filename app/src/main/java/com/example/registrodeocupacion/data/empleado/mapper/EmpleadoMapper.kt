package com.example.registrodeocupacion.data.empleado.mapper

import com.example.registrodeocupacion.data.empleado.local.EmpleadoEntity
import com.example.registrodeocupacion.domain.empleado.model.Empleado

fun EmpleadoEntity.toDomain() : Empleado = Empleado(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo,
)

fun Empleado.toEntity(): EmpleadoEntity = EmpleadoEntity(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo,
)
