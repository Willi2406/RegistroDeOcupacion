package com.example.registrodeocupacion.presentacion.ocupaciones.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.example.registrodeocupacion.domain.ocupacion.model.Ocupacion

@Composable
fun OcupacionListScreen(
    viewModel: OcupacionListViewModel = hiltViewModel(),
    onAddOcupacion: () -> Unit,
    onEditOcupacion: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OcupacionListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onAddClick = onAddOcupacion,
        onEditClick = onEditOcupacion
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcupacionListBody(
    state: OcupacionListUiState,
    onEvent: (OcupacionListUiEvent) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(OcupacionListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ocupacion")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.ocupaciones.isEmpty()) {
                Text(
                    text = "No hay ocupaciones",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Un poco más de espacio entre tarjetas
                ) {
                    items(state.ocupaciones, key = { it.ocupacioneId }) { ocupacion ->
                        OcupacionItem(
                            ocupacion = ocupacion,
                            onEdit = { onEditClick(ocupacion.ocupacioneId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OcupacionItem(
    ocupacion: Ocupacion,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ocupacion.descricion,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )


                val puestoTexto = if (ocupacion.espuestodirecion) "Puesto de Dirección" else "Puesto Regular"
                val colorTexto = if (ocupacion.espuestodirecion) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

                Text(
                    text = puestoTexto,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorTexto,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}