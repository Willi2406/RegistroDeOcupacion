package com.example.registrodeocupacion.domain.horasextra.usecase

import com.example.registrodeocupacion.domain.empleado.usecase.ValidationResult
import java.time.LocalDate
import javax.annotation.meta.When

data class ValidarResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validarFecha(fecha: LocalDate): ValidarResult {
    return when {
        fecha.isAfter(LocalDate.now()) -> ValidarResult(
            false,
            "La fecha de ingreso es obligatoria"
        )

        else -> ValidarResult(true)
    }
}

fun validarCantidadHora(cantidadHoras: String): ValidarResult {
    return when {
        cantidadHoras.isBlank() -> ValidarResult(
            false,
            "La cantidad de horas es obligatoria"
        )
        cantidadHoras.toDoubleOrNull() == null || cantidadHoras.toDouble() <= 0 -> {
            ValidarResult(
                false,
                "La cantidad debe ser un número mayor a 0"
            )
        }
        else -> ValidarResult(true)
    }
}

fun validarTipo(tipo: String): ValidarResult {
    return when {
        tipo.isBlank() -> ValidarResult(
            false,
            "El tipo de hora extra es obligatorio"
        )
        else -> ValidarResult(true)
    }
}

fun validarEmpleado(empleadoId: String): ValidarResult{
    return when {
        empleadoId.isBlank() -> ValidarResult(
            false,
            "El empleado es obligatorio"
        )
        else -> ValidarResult(true)
    }
}