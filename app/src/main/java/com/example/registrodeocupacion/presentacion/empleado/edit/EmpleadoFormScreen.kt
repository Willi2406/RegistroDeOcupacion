package com.example.registrodeocupacion.presentacion.empleado.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.ZoneId
import com.example.registrodeocupacion.data.empleado.local.FrecuenciaPago // Asegúrate de que la ruta del Enum sea la correcta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoFormScreen(
    viewModel: EmpleadoFormViewModel = hiltViewModel(),
    onBack: () -> Unit,
    empleadoId: Int
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var sexoMenuExpanded by remember { mutableStateOf(false) }
    var ocupacionMenuExpanded by remember { mutableStateOf(false) }
    var frecuenciaMenuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(empleadoId) {
        viewModel.loadEmpleado(empleadoId)
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Empleado") },
            text = { Text("¿Estás seguro de que deseas eliminar este empleado?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.onEvent(EmpleadoFormUiEvent.Delete)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Nuevo empleado" else "Editar empleado") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    AnimatedVisibility(visible = !state.isNew) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = state.fechaIngreso.toString(),
                onValueChange = { },
                readOnly = true,
                label = { Text("Fecha de ingreso") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, "Calendario")
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                isError = state.fechaIngresoError != null
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                                viewModel.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(date))
                            }
                            showDatePicker = false
                        }) { Text("Aceptar") }
                    }
                ) { DatePicker(state = datePickerState) }
            }

            OutlinedTextField(
                value = state.nombres,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.NombresChanged(it)) },
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.nombresError != null
            )

            ExposedDropdownMenuBox(
                expanded = sexoMenuExpanded,
                onExpandedChange = { sexoMenuExpanded = !sexoMenuExpanded }
            ) {
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = sexoMenuExpanded, onDismissRequest = { sexoMenuExpanded = false }) {
                    listOf("Masculino", "Femenino").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { viewModel.onEvent(EmpleadoFormUiEvent.SexoChanged(opcion)); sexoMenuExpanded = false }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = ocupacionMenuExpanded,
                onExpandedChange = { ocupacionMenuExpanded = !ocupacionMenuExpanded }
            ) {
                OutlinedTextField(
                    value = state.ocupacionesDisponibles.find { it.ocupacioneId == state.ocupacionId }?.descricion ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ocupación") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ocupacionMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    isError = state.ocupacionIdError != null
                )
                ExposedDropdownMenu(expanded = ocupacionMenuExpanded, onDismissRequest = { ocupacionMenuExpanded = false }) {
                    state.ocupacionesDisponibles.forEach { ocupacion ->
                        DropdownMenuItem(
                            text = { Text(ocupacion.descricion) },
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.OcupacionIdChanged(ocupacion.ocupacioneId))
                                ocupacionMenuExpanded = false
                            }
                        )
                    }
                }
            }


            ExposedDropdownMenuBox(
                expanded = frecuenciaMenuExpanded,
                onExpandedChange = { frecuenciaMenuExpanded = !frecuenciaMenuExpanded }
            ) {
                OutlinedTextField(
                    value = state.frecuenciaPago.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frecuencia de Pago") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frecuenciaMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    isError = state.frecuenciaPagoError != null,
                    supportingText = state.frecuenciaPagoError?.let { errorMsg -> { Text(errorMsg) } }
                )
                ExposedDropdownMenu(
                    expanded = frecuenciaMenuExpanded,
                    onDismissRequest = { frecuenciaMenuExpanded = false }
                ) {
                    FrecuenciaPago.entries.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.name) },
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.FrecuenciaPagoChanged(opcion))
                                frecuenciaMenuExpanded = false
                            }
                        )
                    }
                }
            }


            OutlinedTextField(
                value = state.sueldo,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.SueldoChanged(it)) },
                label = { Text("Sueldo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = state.sueldoError != null
            )

            Button(
                onClick = { viewModel.onEvent(EmpleadoFormUiEvent.Save) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving
            ) {
                Text(if (state.isSaving) "Guardando..." else "Guardar")
            }
        }
    }
}