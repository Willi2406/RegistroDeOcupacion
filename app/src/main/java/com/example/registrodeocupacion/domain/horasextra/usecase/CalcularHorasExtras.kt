package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.data.empleado.local.FrecuenciaPago
import com.example.registrodeocupacion.data.horasextra.local.TipoHoraExtra

fun calcularMontoHoraExtra(
    sueldo: Double,
    frecuenciaDePago: FrecuenciaPago,
    tipoHoraExtra: TipoHoraExtra ,
    cantidadHoras: Int,
    esPuestoDireccion: Boolean

): Double{
    if (esPuestoDireccion){
        return 0.0
    }

    val salarioDiario = sueldo/frecuenciaDePago.dias

    val valorHoraOrdinaria = salarioDiario / 8.0
    val montoTotal = valorHoraOrdinaria * tipoHoraExtra.porcentajerecargo * cantidadHoras

    return Math.round(montoTotal * 100) / 100.0
}