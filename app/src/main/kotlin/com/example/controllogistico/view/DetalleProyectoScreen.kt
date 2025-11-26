package com.example.controllogistico.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.controllogistico.model.ProyectoConSolicitudes
import com.example.controllogistico.viewmodel.DetalleProyectoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProyectoScreen(
    viewModel: DetalleProyectoViewModel,
    navController: NavController
) {
    val proyectoConSolicitudes by viewModel.proyectoConSolicitudes.collectAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(proyectoConSolicitudes?.proyecto?.nombre ?: "Cargando...") },
                navigationIcon = { /* Botón para volver atrás */ }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Text("Líder: ${proyectoConSolicitudes?.proyecto?.lider ?: ""}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                // Aquí iría la gráfica o barra de progreso
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = 0.6f) // Ejemplo estático
                Spacer(modifier = Modifier.height(16.dp))
                Text("Historial de Solicitudes", style = MaterialTheme.typography.titleLarge)
            }
            items(proyectoConSolicitudes?.solicitudes ?: emptyList()) { solicitud ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${solicitud.id}")
                        Text("Estado: ${solicitud.estado}")
                    }
                }
            }
        }
    }
}
