package com.example.registrodeocupacion.presentacion.empleado.list

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
import com.example.registrodeocupacion.domain.empleado.model.Empleado

@Composable
fun EmpleadoListScreen(
    viewModel: EmpleadoListViewModel = hiltViewModel(),
    onAddEmpleado: () -> Unit,
    onEditEmpleado: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmpleadoListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onAddClick = onAddEmpleado,
        onEditClick = onEditEmpleado
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoListBody(
    state: EmpleadoListUiState,
    onEvent: (EmpleadoListUiEvent) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(EmpleadoListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, modifier = Modifier.testTag("emp_add")) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar empleado")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("Cargando"))
            } else if (state.empleados.isEmpty()) {
                Text(text = "No hay empleados registrados", modifier = Modifier.align(Alignment.Center).testTag("Mensaje Vacio"))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = state.empleados, key = { it.empleadoId }) { empleado ->

                        val ocupacionNombre = state.ocupaciones.find { it.ocupacioneId == empleado.ocupacionId }?.descricion ?: "Sin Ocupación"

                        EmpleadoItem(
                            empleado = empleado,
                            ocupacionNombre = ocupacionNombre,
                            onEdit = { onEditClick(empleado.empleadoId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmpleadoItem(
    empleado: Empleado,
    ocupacionNombre: String,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = empleado.nombres,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sexo: ${empleado.sexo}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Ingreso: ${empleado.fechaIngreso}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Frecuencia de pago: ${empleado.frecuenciaPago.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )


                Text(
                    text = "Ocupación: $ocupacionNombre",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "RD$ ${empleado.sueldo}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}