package com.example.registrodeocupacion.data.mapper

import com.example.registrodeocupacion.data.local.OcupacioneEntity
import com.example.registrodeocupacion.domain.model.Ocupacion

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