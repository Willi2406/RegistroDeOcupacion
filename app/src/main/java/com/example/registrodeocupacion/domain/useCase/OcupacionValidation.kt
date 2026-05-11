package com.example.registrodeocupacion.domain.useCase

data class OcupacionValidation(
    val isValid: Boolean,
    val error: String? = null
)

fun validateDescription(descripcion: String, ocupacioneExistentes: List<String>):
        OcupacionValidation{
    return when{
        descripcion.isBlank() -> OcupacionValidation(isValid = false,
            error = "Llena lo que esta aqui, no puede estar vacio como tu corazon")
        descripcion.length <3 -> OcupacionValidation(isValid = false,
            error = "Tiene que tener mas de 3 caracteres para que sea mas grande")
        ocupacioneExistentes.any{it.equals( other= descripcion.trim(), ignoreCase = true)}
            -> OcupacionValidation( isValid = false,
            error = "La descripcion esta duplicada por favor ingresar otra descnipcion")
        else -> OcupacionValidation( isValid = true)
    }
}

fun validateSueldo(sueldo: String): OcupacionValidation {
    return when {
        sueldo.isBlank() -> OcupacionValidation(
            isValid = false,
            error = "El sueldo no puede estar vacio tu no cobra es"
        )

        sueldo.toDoubleOrNull() == null -> OcupacionValidation(
            isValid = false,
            "Ingrese un sueldo valido"
        )

        sueldo.toDouble() <= 0.0 -> OcupacionValidation(
            isValid = false,
            error = "El sueldo tiene que ser mayor que cero"
        )

        else -> OcupacionValidation(isValid = true)
    }
}