package com.example.controllogistico.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controllogistico.repository.InventarioRepository

@Composable
fun HomeScreen(
    repository: InventarioRepository,
    onNavigateToInventario: () -> Unit,
    onNavigateToNuevaSolicitud: () -> Unit
) {
    val materiales by repository.materiales.collectAsState()
    val materialesEnAlertaRoja = materiales.count { it.stockDisponible <= it.minStock }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        KpiWidget(materialesEnAlertaRoja)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToInventario,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Ver Inventario", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToNuevaSolicitud,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Nueva Solicitud", fontSize = 18.sp)
        }
    }
}

@Composable
fun KpiWidget(count: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Materiales en Alerta Roja:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "$count",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = if (count > 0) Color.Red else Color.Unspecified
            )
        }
    }
}
