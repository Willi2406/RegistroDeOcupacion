package com.example.registrodeocupacion.presentacion.horasextras.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registrodeocupacion.domain.horasextra.model.HoraExtra

@Composable
fun HorasExtrasListScreen(
    viewModel: HorasExtrasListViewModel = hiltViewModel(),
    onAddHoraExtra: () -> Unit,
    onEditHoraExtra: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HorasExtrasListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onAddClick = onAddHoraExtra,
        onEditClick = onEditHoraExtra
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorasExtrasListBody(
    state: HorasExtrasListUiState,
    onEvent: (HorasExtrasListUiEvent) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(HorasExtrasListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, modifier = Modifier.testTag("he_add")) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Horas Extras")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("Cargando"))
            } else if (state.horasExtras.isEmpty()) {
                Text(text = "No hay horas extras registradas", modifier = Modifier.align(Alignment.Center).testTag("Mensaje_Vacio"))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = state.horasExtras, key = { it.horasExtraId }) { horaExtra ->

                        // MAGIA AQUÍ: Buscamos el nombre del empleado
                        val empleadoNombre = state.empleados.find { it.empleadoId == horaExtra.empleadoId }?.nombres ?: "Empleado Desconocido"

                        HoraExtraItem(
                            horaExtra = horaExtra,
                            empleadoNombre = empleadoNombre, // Pasamos el nombre real
                            onEdit = { onEditClick(horaExtra.horasExtraId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HoraExtraItem(
    horaExtra: HoraExtra,
    empleadoNombre: String,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onEdit() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = empleadoNombre, // ¡Aparece el nombre del empleado!
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Fecha: ${horaExtra.fecha}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Horas trabajadas: ${horaExtra.cantidadHoras}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 2.dp)
            )

            Text(
                text = "Tipo: ${horaExtra.tipo.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 2.dp)
            )

            Text(
                text = "Total Pago: RD$ ${horaExtra.recargo}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}