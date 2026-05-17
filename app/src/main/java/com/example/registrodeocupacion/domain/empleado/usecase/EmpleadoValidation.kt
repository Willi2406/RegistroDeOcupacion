package com.example.registrodeocupacion.domain.empleado.usecase


import java.time.LocalDate

data class ValidationResult(
    val isValid: Boolean,
    val error: String?  = null
)

fun validarNombres(nombres: String): ValidationResult {
    return when{
        nombres.isBlank() -> ValidationResult(false, "El nombre es obligatorio")
        nombres.length < 2 -> ValidationResult(false, "Minimo 2 caracteres")
        !nombres.all {it.isLetter()} -> ValidationResult(false, "El nombre solo debe contener letras")
        else -> ValidationResult(true)
    }
}

fun validarSueldo(sueldo: String): ValidationResult {
    return when{
        sueldo.isBlank() -> ValidationResult(false, "El sueldo es obligatorio")
        sueldo.toDoubleOrNull() == null -> ValidationResult(false, "El sueldo debe ser un numero")
        sueldo.toDouble() <= 0 -> ValidationResult(false, "El sueldo debe ser mayor a 0")
        else -> ValidationResult(true)
    }
}

fun validarSexo(sexo: String): ValidationResult{
    return when{
        sexo.isBlank() -> ValidationResult(false, "El sexo es obligatorio")
        else -> ValidationResult(true)
    }
}

fun validarFecha(fecha: LocalDate): ValidationResult{
    return when{
        fecha.isAfter(LocalDate.now()) -> ValidationResult(false, "La fecha de ingreso es obligatoria")
        else -> ValidationResult(true)
    }
}