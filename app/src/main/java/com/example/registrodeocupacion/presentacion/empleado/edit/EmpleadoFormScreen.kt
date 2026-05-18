package com.example.registrodeocupacion.presentacion.empleado.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // Importación corregida para Compose Navigation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoFormScreen(
    viewModel: EmpleadoFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Nuevo empleado" else "Editar empleado") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
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
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
                    .testTag("input_fecha_ingreso"),
                isError = state.fechaIngresoError != null,
                supportingText = state.fechaIngresoError?.let { errorMsg -> { Text(errorMsg) } }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                viewModel.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(date))
                            }
                            showDatePicker = false
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = state.nombres,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.NombresChanged(it)) },
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth().testTag("input_nombres"),
                isError = state.nombresError != null,
                supportingText = state.nombresError?.let { errorMsg -> { Text(errorMsg) } },
                singleLine = true
            )

            val opcionesSexo = listOf("Masculino", "Femenino")
            ExposedDropdownMenuBox(
                expanded = menuExpanded,
                onExpandedChange = { menuExpanded = !menuExpanded }
            ) {
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor().testTag("input_sexo"),
                    isError = state.sexoError != null,
                    supportingText = state.sexoError?.let { errorMsg -> { Text(errorMsg) } }
                )
                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    opcionesSexo.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                viewModel.onEvent(EmpleadoFormUiEvent.SexoChanged(opcion))
                                menuExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.sueldo,
                onValueChange = { viewModel.onEvent(EmpleadoFormUiEvent.SueldoChanged(it)) },
                label = { Text("Sueldo") },
                modifier = Modifier.fillMaxWidth().testTag("input_sueldo"),
                isError = state.sueldoError != null,
                supportingText = state.sueldoError?.let { errorMsg -> { Text(errorMsg) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Button(
                onClick = { viewModel.onEvent(EmpleadoFormUiEvent.Save) },
                modifier = Modifier.fillMaxWidth().testTag("btn_save"),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}