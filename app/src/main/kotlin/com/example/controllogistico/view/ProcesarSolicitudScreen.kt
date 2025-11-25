package com.example.controllogistico.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controllogistico.model.SolicitudConDetalles
import com.example.controllogistico.viewmodel.ProcesarSolicitudViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProcesarSolicitudScreen(viewModel: ProcesarSolicitudViewModel) {
    val solicitudes by viewModel.solicitudesPendientes.collectAsState()
    var solicitudSeleccionada by remember { mutableStateOf<SolicitudConDetalles?>(null) }

    if (solicitudSeleccionada == null) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Solicitudes Pendientes de Aprobación", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(solicitudes) { solicitud ->
                SolicitudCard(solicitud = solicitud) {
                    solicitudSeleccionada = it
                }
            }
        }
    } else {
        DetalleSolicitudScreen(solicitud = solicitudSeleccionada!!) {
            // Asumimos que la lógica de aprobación ocurre aquí antes de procesar
            viewModel.onProcesarSolicitud(it.solicitud.id)
            solicitudSeleccionada = null
        }
    }
}

@Composable
fun SolicitudCard(solicitud: SolicitudConDetalles, onClick: (SolicitudConDetalles) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(solicitud) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Solicitud: ${solicitud.solicitud.id}", style = MaterialTheme.typography.titleMedium)
            Text("Proyecto: ${solicitud.solicitud.proyectoId}")
            Text("Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(solicitud.solicitud.fecha))}")
            Text("Items: ${solicitud.detalles.size}")
        }
    }
}

@Composable
fun DetalleSolicitudScreen(solicitud: SolicitudConDetalles, onProcesar: (SolicitudConDetalles) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalle de Solicitud", style = MaterialTheme.typography.titleLarge)
        // ... aquí iría una vista más detallada de los items
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onProcesar(solicitud) }) {
            Text("Aprobar y Procesar en Bodega")
        }
    }
}
