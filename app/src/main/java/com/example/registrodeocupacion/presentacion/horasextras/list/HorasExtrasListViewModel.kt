package com.example.registrodeocupacion.presentacion.horasextras.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrodeocupacion.domain.empleado.usecase.ObserveEmpleadoUseCase
import com.example.registrodeocupacion.domain.horasextra.usecase.ObserveHorasExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorasExtrasListViewModel @Inject constructor(
    private val observeHorasExtrasUseCase: ObserveHorasExtrasUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HorasExtrasListUiState(isLoading = true))
    val state: StateFlow<HorasExtrasListUiState> = _state.asStateFlow()

    init {
        loadHorasExtras()
        loadEmpleados()
    }

    fun onEvent(event: HorasExtrasListUiEvent) {
        when (event) {
            HorasExtrasListUiEvent.Load -> {
                loadHorasExtras()
                loadEmpleados()
            }
            HorasExtrasListUiEvent.Refresh -> {
                loadHorasExtras()
                loadEmpleados()
            }
            is HorasExtrasListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            HorasExtrasListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            HorasExtrasListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is HorasExtrasListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            else -> {}
        }
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            observeEmpleadoUseCase().collect { lista ->
                _state.update { it.copy(empleados = lista) }
            }
        }
    }

    private fun loadHorasExtras() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeHorasExtrasUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        horasExtras = list,
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