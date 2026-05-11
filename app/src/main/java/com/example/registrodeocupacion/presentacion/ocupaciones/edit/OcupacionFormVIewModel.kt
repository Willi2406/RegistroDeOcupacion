package com.example.registrodeocupacion.presentacion.ocupaciones.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.registrodeocupacion.domain.model.Ocupacion
import com.example.registrodeocupacion.domain.useCase.DeleteOcupacionUseCase
import com.example.registrodeocupacion.domain.useCase.GetOcupacionUseCase
import com.example.registrodeocupacion.domain.useCase.UpsertOcupacionUseCase
import com.example.registrodeocupacion.presentacion.navegation.Screen

import javax.inject.Inject

@HiltViewModel
class OcupacionFormViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertOcupacionUseCase: UpsertOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.OcupacionForm>()
    private val ocupacionId: Int = routeArgs.ocupacionId

    private val _state = MutableStateFlow(OcupacionFormUiState())
    val state: StateFlow<OcupacionFormUiState> = _state.asStateFlow()

    init {
        loadOcupacion(ocupacionId)
    }

    fun onEvent(event: OcupacionFormUiEvent) {
        when (event) {
            is OcupacionFormUiEvent.Load -> loadOcupacion(event.id)
            is OcupacionFormUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.value, descripcionError = null)
            }
            is OcupacionFormUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            OcupacionFormUiEvent.Save -> onSave()
            OcupacionFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadOcupacion(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }

        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)
            if (ocupacion != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        ocupacionId = ocupacion.ocupacioneId,
                        descripcion = ocupacion.descricion,
                        sueldo = ocupacion.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, ocupacionId = null) }
            }
        }
    }

    private fun onSave() {
        val descripcion = state.value.descripcion
        val sueldoText = state.value.sueldo

        val descripcionValidation = validateDescripcion(descripcion)
        val sueldoValidation = validateSueldo(sueldoText)

        if (!descripcionValidation.isValid || !sueldoValidation.isValid) {
            _state.update {
                it.copy(
                    descripcionError = descripcionValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val ocupacion = Ocupacion(
                ocupacioneId = state.value.ocupacionId ?: 0,
                descricion = descripcion,
                sueldo = sueldoText.toDouble()
            )

            val result = upsertOcupacionUseCase(ocupacion)

            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        ocupacionId = newId,
                        isNew = false
                    )
                }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteOcupacionUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }

    private fun validateDescripcion(descripcion: String): ValidationResult {
        if (descripcion.isBlank()) {
            return ValidationResult(isValid = false, error = "La descricion no puede estar vacia")
        }
        return ValidationResult(isValid = true)
    }

    private fun validateSueldo(sueldo: String): ValidationResult {
        if (sueldo.isBlank()) {
            return ValidationResult(isValid = false, error = "El sueldo no puede estar vacio")
        }

        val sueldoDouble = sueldo.toDoubleOrNull()

        if (sueldoDouble == null || sueldoDouble <= 0) {
            return ValidationResult(isValid = false, error = "Ingrese un monto valido mayor a 0")
        }
        return ValidationResult(isValid = true)
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)