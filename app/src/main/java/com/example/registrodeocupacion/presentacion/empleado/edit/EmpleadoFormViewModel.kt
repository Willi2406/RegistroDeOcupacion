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
import com.example.registrodeocupacion.data.empleado.local.FrecuenciaPago // Asegúrate que esta sea la ruta real
import com.example.registrodeocupacion.domain.empleado.usecase.*
import com.example.registrodeocupacion.domain.ocupacion.usecase.ObserveOcupacionesUseCase
import com.example.registrodeocupacion.presentacion.navegation.Screen

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    private val observeOcupacionesUseCase: ObserveOcupacionesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.EmpleadoForm>()
    private val empleadoId: Int = routeArgs.empleadoId

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    init {
        loadOcupaciones()
    }

    fun onEvent(event: EmpleadoFormUiEvent) {
        when (event) {
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
            is EmpleadoFormUiEvent.NombresChanged -> _state.update { it.copy(nombres = event.value, nombresError = null) }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update { it.copy(sexo = event.value, sexoError = null) }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update { it.copy(sueldo = event.value, sueldoError = null) }

            is EmpleadoFormUiEvent.FrecuenciaPagoChanged -> _state.update {
                it.copy(frecuenciaPago = event.value, frecuenciaPagoError = null)
            }

            is EmpleadoFormUiEvent.OcupacionIdChanged -> _state.update { it.copy(ocupacionId = event.value, ocupacionIdError = null) }

            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadOcupaciones() {
        viewModelScope.launch {
            observeOcupacionesUseCase().collect { listaOcupaciones ->
                _state.update { it.copy(ocupacionesDisponibles = listaOcupaciones) }
            }
        }
    }

    fun loadEmpleado(id: Int) {
        if( id == 0 ){
            val ocupaciones = _state.value.ocupacionesDisponibles
            _state.value = EmpleadoFormUiState(ocupacionesDisponibles = ocupaciones)
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
                        sueldo = empleado.sueldo.toString(),
                        frecuenciaPago = empleado.frecuenciaPago,
                        ocupacionId = empleado.ocupacionId
                    )
                }
            }
        }
    }

    private fun onSave() {
        val s = state.value

        val fechaResult = validarFecha(s.fechaIngreso)
        val nombresResult = validarNombres(s.nombres)
        val sexoResult = validarSexo(s.sexo)
        val sueldoResult = validarSueldo(s.sueldo)


        val ocupacionError = if(s.ocupacionId == null || s.ocupacionId == 0) "Seleccione una ocupación" else null


        val frecuenciaError = if(s.frecuenciaPago == null) "Seleccione una frecuencia" else null

        if (!fechaResult.isValid || !nombresResult.isValid || !sexoResult.isValid || !sueldoResult.isValid || frecuenciaError != null || ocupacionError != null) {
            _state.update {
                it.copy(
                    fechaIngresoError = fechaResult.error,
                    nombresError = nombresResult.error,
                    sexoError = sexoResult.error,
                    sueldoError = sueldoResult.error,
                    frecuenciaPagoError = frecuenciaError,
                    ocupacionIdError = ocupacionError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val empleado = Empleado(
                empleadoId = s.empleadoId ?: 0,
                fechaIngreso = s.fechaIngreso,
                nombres = s.nombres,
                sexo = s.sexo,
                sueldo = s.sueldo.toDoubleOrNull() ?: 0.0,
                frecuenciaPago = s.frecuenciaPago!!,
                ocupacionId = s.ocupacionId ?: 0
            )

            upsertEmpleadoUseCase(empleado)
                .onSuccess { newId -> _state.update { it.copy(isSaving = false, saved = true, empleadoId = newId) } }
                .onFailure { _state.update { it.copy(isSaving = false) } }
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