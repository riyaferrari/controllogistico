package com.example.controllogistico.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controllogistico.viewmodel.MisSolicitudesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisSolicitudesScreen(
    viewModel: MisSolicitudesViewModel
) {
    val solicitudes by viewModel.todasLasSolicitudes.collectAsState(emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Solicitudes") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(solicitudes) { solicitud ->
                SolicitudCard(solicitud = solicitud, onClick = { /* Navegar al detalle si se desea */ })
            }
        }
    }
}
