package com.example.registrodeocupacion.data.ocupacion.mapper

import com.example.registrodeocupacion.data.ocupacion.local.OcupacioneEntity
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion

fun OcupacioneEntity.toDomain() : Ocupacion = Ocupacion(
    ocupacioneId = ocupacioneId,
    descricion = descricion,
    sueldo = sueldo,

    )

fun Ocupacion.toEntity() : OcupacioneEntity = OcupacioneEntity(
    ocupacioneId = ocupacioneId,
    descricion = descricion,
    sueldo = sueldo,

    )