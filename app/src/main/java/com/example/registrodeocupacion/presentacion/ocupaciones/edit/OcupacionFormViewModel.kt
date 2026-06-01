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
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import com.example.registrodeocupacion.domain.ocupacion.usecase.DeleteOcupacionUseCase
import com.example.registrodeocupacion.domain.ocupacion.usecase.GetOcupacionUseCase
import com.example.registrodeocupacion.domain.ocupacion.usecase.UpsertOcupacionUseCase
import com.example.registrodeocupacion.domain.ocupacion.usecase.validateDescription
// Se eliminó la importación de validateSueldo
import com.example.registrodeocupacion.presentacion.navegation.Screen
import kotlinx.coroutines.flow.first

import javax.inject.Inject

@HiltViewModel
class OcupacionFormViewModel @Inject constructor(
    private val repository: OcupacionRepository,
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

            is OcupacionFormUiEvent.EsPuestoDireccionChanged -> _state.update {
                it.copy(esPuestoDireccion = event.value)
            }

            OcupacionFormUiEvent.Save -> onSave()
            OcupacionFormUiEvent.Delete -> onDelete()
        }
    }

    fun loadOcupacion(id: Int) {
        if (id == 0) {
            _state.value = OcupacionFormUiState()
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
                        esPuestoDireccion = ocupacion.espuestodirecion
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, ocupacionId = null) }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val descripcion = state.value.descripcion
            val esPuestoDireccion = state.value.esPuestoDireccion

            val descripcionesExistentes = repository.observeOcupaciones()
                .first()
                .map { it.descricion }

            val descripcionValidation = validateDescription(descripcion, descripcionesExistentes)


            if (!descripcionValidation.isValid) {
                _state.update {
                    it.copy(
                        descripcionError = descripcionValidation.error
                    )
                }
                return@launch
            }
            _state.update { it.copy(isSaving = true) }

            val ocupacion = Ocupacion(
                ocupacioneId = state.value.ocupacionId ?: 0,
                descricion = descripcion,
                espuestodirecion = esPuestoDireccion
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
}