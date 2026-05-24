package com.example.registrodeocupacion.domain.ocupacion.usecase

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
            error = "La descripcion esta duplicada por favor ingresar otra mierda")
        else -> OcupacionValidation( isValid = true)
    }
}
