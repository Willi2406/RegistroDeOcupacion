package com.example.registrodeocupacion.presentacion.empleado.edit

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
import javax.inject.Inject
import com.example.registrodeocupacion.domain.empleado.model.Empleado
import com.example.registrodeocupacion.domain.empleado.usecase.DeleteEmpleadoUseCase
import com.example.registrodeocupacion.domain.empleado.usecase.GetEmpleadoUseCase
import com.example.registrodeocupacion.domain.empleado.usecase.UpsertEmpleadoUseCase
import com.example.registrodeocupacion.domain.empleado.usecase.validarFecha
import com.example.registrodeocupacion.domain.empleado.usecase.validarNombres
import com.example.registrodeocupacion.domain.empleado.usecase.validarSexo
import com.example.registrodeocupacion.domain.empleado.usecase.validarSueldo
import com.example.registrodeocupacion.presentacion.navegation.Screen

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoForm>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    init {
        loadEmpleado(empleadoId)
    }

    fun onEvent(event: EmpleadoFormUiEvent) {
        when (event) {
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
            is EmpleadoFormUiEvent.NombresChanged -> _state.update { it.copy(nombres = event.value, nombresError = null) }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update { it.copy(sexo = event.value, sexoError = null) }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update { it.copy(sueldo = event.value, sueldoError = null) }
            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadEmpleado(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }

        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            if (empleado != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = empleado.empleadoId,
                        fechaIngreso = empleado.fechaIngreso,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        sueldo = empleado.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, empleadoId = null) }
            }
        }
    }

    private fun onSave() {
        val fechaIngreso = state.value.fechaIngreso
        val nombres = state.value.nombres
        val sexo = state.value.sexo
        val sueldoText = state.value.sueldo

        val fechaIngresoValidation = validarFecha(fechaIngreso)
        val nombresValidation = validarNombres(nombres)
        val sexoValidation = validarSexo(sexo)
        val sueldoValidation = validarSueldo(sueldoText)

        if (!fechaIngresoValidation.isValid || !nombresValidation.isValid || !sexoValidation.isValid || !sueldoValidation.isValid) {
            _state.update {
                it.copy(
                    fechaIngresoError = fechaIngresoValidation.error,
                    nombresError = nombresValidation.error,
                    sexoError = sexoValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val empleado = Empleado(
                empleadoId = state.value.empleadoId ?: 0,
                fechaIngreso = fechaIngreso,
                nombres = nombres,
                sexo = sexo,
                sueldo = sueldoText.toDouble()
            )

            val result = upsertEmpleadoUseCase(empleado)

            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        empleadoId = newId,
                        isNew = false
                    )
                }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteEmpleadoUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}