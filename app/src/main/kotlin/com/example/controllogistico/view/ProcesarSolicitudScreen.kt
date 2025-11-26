package com.example.controllogistico.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controllogistico.model.SolicitudConDetalles
import com.example.controllogistico.viewmodel.ProcesarSolicitudViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProcesarSolicitudScreen(
    viewModel: ProcesarSolicitudViewModel,
    onSolicitudProcesada: () -> Unit
) {
    val solicitudes by viewModel.solicitudesPendientes.collectAsState()
    var solicitudSeleccionada by remember { mutableStateOf<SolicitudConDetalles?>(null) }

    if (solicitudSeleccionada == null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Solicitudes Pendientes de Aprobación", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(solicitudes) { solicitud ->
                    SolicitudCard(solicitud = solicitud) {
                        solicitudSeleccionada = it
                    }
                }
            }
        }
    } else {
        DetalleSolicitudScreen(
            solicitud = solicitudSeleccionada!!,
            onProcesar = {
                viewModel.procesarSolicitud(it.solicitud.id)
                solicitudSeleccionada = null
                onSolicitudProcesada()
            },
            onBack = { solicitudSeleccionada = null }
        )
    }
}

@Composable
fun SolicitudCard(solicitud: SolicitudConDetalles, onClick: (SolicitudConDetalles) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(solicitud) },
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
fun DetalleSolicitudScreen(
    solicitud: SolicitudConDetalles,
    onProcesar: (SolicitudConDetalles) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalle de Solicitud", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("ID: ${solicitud.solicitud.id}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { onProcesar(solicitud) }) {
                Text("Aprobar y Procesar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onBack) {
                Text("Volver a la Lista")
            }
        }
    }
}
