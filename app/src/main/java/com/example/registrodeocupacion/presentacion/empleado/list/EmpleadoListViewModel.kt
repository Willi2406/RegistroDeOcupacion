package com.example.registrodeocupacion.presentacion.empleado.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.registrodeocupacion.domain.empleado.usecase.ObserveEmpleadoUseCase
import com.example.registrodeocupacion.domain.ocupacion.usecase.ObserveOcupacionesUseCase

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    private val observeOcupacionesUseCase: ObserveOcupacionesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoListUiState(isLoading = true))
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
        loadOcupaciones()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when (event) {
            EmpleadoListUiEvent.Load -> {
                loadEmpleados()
                loadOcupaciones()
            }
            EmpleadoListUiEvent.Refresh -> {
                loadEmpleados()
                loadOcupaciones()
            }
            is EmpleadoListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            EmpleadoListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            EmpleadoListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EmpleadoListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            else -> {}
        }
    }


    private fun loadOcupaciones() {
        viewModelScope.launch {
            observeOcupacionesUseCase().collect { lista ->
                _state.update { it.copy(ocupaciones = lista) }
            }
        }
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeEmpleadoUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        empleados = list,
                        message = null
                    )
                }
            }
        }
    }

    fun onNavigationDone() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}