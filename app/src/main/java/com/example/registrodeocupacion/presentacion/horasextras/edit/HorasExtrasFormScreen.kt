package com.example.registrodeocupacion.presentacion.horasextras.edit

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
import com.example.registrodeocupacion.data.horasextra.local.TipoHoraExtra
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorasExtrasFormScreen(
    viewModel: HorasExtrasFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Estados para los menús desplegables
    var empleadoMenuExpanded by remember { mutableStateOf(false) }
    var tipoMenuExpanded by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Manejo de navegación al guardar o eliminar
    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Registro") },
            text = { Text("¿Estás seguro de que deseas eliminar este registro de horas extras?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.onEvent(HorasExtrasFormUiEvent.Delete)
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
                title = { Text(if (state.isNew) "Registrar Horas Extras" else "Editar Horas Extras") },
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

            // 1. Selector de Empleado
            ExposedDropdownMenuBox(
                expanded = empleadoMenuExpanded,
                onExpandedChange = { empleadoMenuExpanded = !empleadoMenuExpanded }
            ) {
                OutlinedTextField(
                    // Buscamos el nombre del empleado seleccionado para mostrarlo en el TextField
                    value = state.empleadosDisponibles.find { it.empleadoId == state.empleadoId }?.nombres ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Empleado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = empleadoMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    isError = state.empleadoIdError != null,
                    supportingText = state.empleadoIdError?.let { errorMsg -> { Text(errorMsg) } }
                )
                ExposedDropdownMenu(
                    expanded = empleadoMenuExpanded,
                    onDismissRequest = { empleadoMenuExpanded = false }
                ) {
                    state.empleadosDisponibles.forEach { empleado ->
                        DropdownMenuItem(
                            text = { Text(empleado.nombres) },
                            onClick = {
                                viewModel.onEvent(HorasExtrasFormUiEvent.EmpleadoIdChanged(empleado.empleadoId))
                                empleadoMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // 2. Selector de Fecha
            OutlinedTextField(
                value = state.fecha.toString(),
                onValueChange = { },
                readOnly = true,
                label = { Text("Fecha") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, "Calendario")
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                isError = state.fechaError != null
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                                viewModel.onEvent(HorasExtrasFormUiEvent.FechaChanged(date))
                            }
                            showDatePicker = false
                        }) { Text("Aceptar") }
                    }
                ) { DatePicker(state = datePickerState) }
            }

            // 3. Cantidad de Horas
            OutlinedTextField(
                value = state.cantidadHoras,
                onValueChange = { viewModel.onEvent(HorasExtrasFormUiEvent.CantidadHorasChanged(it)) },
                label = { Text("Cantidad de Horas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = state.cantidadHorasError != null,
                supportingText = state.cantidadHorasError?.let { errorMsg -> { Text(errorMsg) } }
            )

            // 4. Selector de Tipo de Hora Extra
            ExposedDropdownMenuBox(
                expanded = tipoMenuExpanded,
                onExpandedChange = { tipoMenuExpanded = !tipoMenuExpanded }
            ) {
                OutlinedTextField(
                    value = state.tipo.descripcion, // Mostramos la descripción bonita (ej: "Diurno")
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Hora Extra") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = tipoMenuExpanded,
                    onDismissRequest = { tipoMenuExpanded = false }
                ) {
                    TipoHoraExtra.entries.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.descripcion) },
                            onClick = {
                                viewModel.onEvent(HorasExtrasFormUiEvent.TipoChanged(opcion))
                                tipoMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // 5. Recargo
            OutlinedTextField(
                value = state.recargo,
                onValueChange = { viewModel.onEvent(HorasExtrasFormUiEvent.RecargoChanged(it)) },
                label = { Text("Recargo Aplicado (Monto)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = state.recargoError != null,
                supportingText = state.recargoError?.let { errorMsg -> { Text(errorMsg) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Botón Guardar
            Button(
                onClick = { viewModel.onEvent(HorasExtrasFormUiEvent.Save) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Horas Extras")
                }
            }
        }
    }
}