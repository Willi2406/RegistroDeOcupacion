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
import com.example.registrodeocupacion.domain.empleado.usecase.DeleteEmpleadoUseCase
import com.example.registrodeocupacion.domain.empleado.usecase.ObserveEmpleadoUseCase

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoListUiState(isLoading = true))
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when (event) {
            EmpleadoListUiEvent.Load -> loadEmpleados()
            EmpleadoListUiEvent.Refresh -> loadEmpleados()
            is EmpleadoListUiEvent.Delete -> onDelete(event.id)
            is EmpleadoListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            EmpleadoListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            EmpleadoListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EmpleadoListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
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
    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteEmpleadoUseCase(id)
            onEvent(EmpleadoListUiEvent.ShowMessage("Empleado Eliminado"))
        }
    }
    fun onNavigationDone() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}