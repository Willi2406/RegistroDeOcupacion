package com.example.registrodeocupacion.data.horasextra.mapper

import com.example.registrodeocupacion.data.horasextra.local.HorasExtraEntity
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra


fun HorasExtraEntity.toDomain() : HoraExtra = HoraExtra(
    horasExtraId = horasExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipo = tipo,
    recargo = recargo
)

fun HoraExtra.toEntity(): HorasExtraEntity = HorasExtraEntity(
    horasExtraId = horasExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipo = tipo,
    recargo = recargo
)