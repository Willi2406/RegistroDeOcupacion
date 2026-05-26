package com.example.registrodeocupacion.presentacion.horasextras.edit

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
import com.example.registrodeocupacion.domain.empleado.usecase.ObserveEmpleadoUseCase
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra
import com.example.registrodeocupacion.domain.horasextra.usecase.DeleteHorasExtrasUseCase
import com.example.registrodeocupacion.domain.horasextra.usecase.GetHorasExtrasUseCase
import com.example.registrodeocupacion.domain.horasextra.usecase.UpsertHorasExtrasUseCase
import com.example.registrodeocupacion.presentacion.navegation.Screen

@HiltViewModel
class HorasExtrasFormViewModel @Inject constructor(
    private val getHorasExtrasUseCase: GetHorasExtrasUseCase,
    private val upsertHorasExtrasUseCase: UpsertHorasExtrasUseCase,
    private val deleteHorasExtrasUseCase: DeleteHorasExtrasUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val routeArgs = savedStateHandle.toRoute<Screen.HorasExtrasForm>()
    private val horasExtraId: Int = routeArgs.horasExtraId

    private val _state = MutableStateFlow(HorasExtrasFormUiState())
    val state: StateFlow<HorasExtrasFormUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
        loadHoraExtra(horasExtraId)
    }

    fun onEvent(event: HorasExtrasFormUiEvent) {
        when (event) {
            is HorasExtrasFormUiEvent.Load -> loadHoraExtra(event.id)
            is HorasExtrasFormUiEvent.EmpleadoIdChanged -> _state.update { it.copy(empleadoId = event.value, empleadoIdError = null) }
            is HorasExtrasFormUiEvent.FechaChanged -> _state.update { it.copy(fecha = event.value, fechaError = null) }
            is HorasExtrasFormUiEvent.CantidadHorasChanged -> _state.update { it.copy(cantidadHoras = event.value, cantidadHorasError = null) }
            is HorasExtrasFormUiEvent.TipoChanged -> _state.update { it.copy(tipo = event.value) }
            is HorasExtrasFormUiEvent.RecargoChanged -> _state.update { it.copy(recargo = event.value, recargoError = null) }
            HorasExtrasFormUiEvent.Save -> onSave()
            HorasExtrasFormUiEvent.Calcular -> onCalcular()
            HorasExtrasFormUiEvent.Delete -> onDelete()
            else -> {}
        }
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            observeEmpleadoUseCase().collect { listaEmpleados ->
                _state.update { it.copy(empleadosDisponibles = listaEmpleados) }
            }
        }
    }

    private fun loadHoraExtra(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, horasExtraId = null) }
            return
        }

        viewModelScope.launch {
            val horaExtra = getHorasExtrasUseCase(id)
            if (horaExtra != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        horasExtraId = horaExtra.horasExtraId,
                        empleadoId = horaExtra.empleadoId,
                        fecha = horaExtra.fecha,
                        cantidadHoras = horaExtra.cantidadHoras.toString(),
                        tipo = horaExtra.tipo,
                        recargo = horaExtra.recargo.toString()
                    )
                }
            }
        }
    }

    private fun onSave() {
        val s = state.value

        // Validaciones manuales básicas
        val empleadoError = if (s.empleadoId == null || s.empleadoId == 0) "Seleccione un empleado" else null
        val horasError = if (s.cantidadHoras.isBlank() || s.cantidadHoras.toDoubleOrNull() == null) "Ingrese una cantidad valida" else null
        val recargoError = if (s.recargo.isBlank() || s.recargo.toDoubleOrNull() == null) "Ingrese un recargo valido" else null

        if (empleadoError != null || horasError != null || recargoError != null) {
            _state.update {
                it.copy(
                    empleadoIdError = empleadoError,
                    cantidadHorasError = horasError,
                    recargoError = recargoError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val horaExtra = HoraExtra(
                horasExtraId = s.horasExtraId ?: 0,
                empleadoId = s.empleadoId ?: 0,
                fecha = s.fecha,
                cantidadHoras = s.cantidadHoras.toDoubleOrNull() ?: 0.0,
                tipo = s.tipo,
                recargo = s.recargo.toDoubleOrNull() ?: 0.0
            )

            upsertHorasExtrasUseCase(horaExtra)
                .onSuccess { newId ->
                    _state.update { it.copy(isSaving = false, saved = true, horasExtraId = newId) }
                }
                .onFailure {
                    _state.update { it.copy(isSaving = false) }
                }
        }
    }

    private fun onDelete() {
        val id = state.value.horasExtraId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteHorasExtrasUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }

    private fun onCalcular() {
        val s = state.value

        val empleado = s.empleadosDisponibles.find { it.empleadoId == s.empleadoId }
        if (empleado == null) {
            _state.update { it.copy(empleadoIdError = "Seleccione un empleado para calcular") }
            return
        }

        val horas = s.cantidadHoras.toDoubleOrNull() ?: 0.0
        if (horas <= 0.0) {
            _state.update { it.copy(cantidadHorasError = "Ingrese las horas para calcular") }
            return
        }

        val sueldoPorHora = empleado.sueldo / 23.83 / 8.0
        val totalRecargo = sueldoPorHora * horas * s.tipo.porcentajerecargo

        _state.update {
            it.copy(
                recargo = String.format("%.2f", totalRecargo),
                empleadoIdError = null,
                cantidadHorasError = null
            )
        }
    }
}