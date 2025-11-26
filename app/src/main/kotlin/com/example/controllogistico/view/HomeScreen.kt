package com.example.controllogistico.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.controllogistico.viewmodel.HomeViewModel
import com.example.controllogistico.viewmodel.KpiUiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val kpiState by viewModel.kpiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Dashboard de Logística", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        KpiRow(kpiState = kpiState)
    }
}

@Composable
fun KpiRow(kpiState: KpiUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        KpiCard(label = "Valor Inventario", value = "$${"%,.2f".format(kpiState.valorInventario)}")
        KpiCard(label = "Solicitudes Pend.", value = "${kpiState.solicitudesPendientes}", isAlert = kpiState.solicitudesPendientes > 0)
        KpiCard(label = "Alertas Stock", value = "${kpiState.alertasStockBajo}", isAlert = kpiState.alertasStockBajo > 0)
    }
}

@Composable
fun KpiCard(label: String, value: String, isAlert: Boolean = false) {
    ElevatedCard(
        modifier = Modifier.size(width = 110.dp, height = 90.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAlert) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isAlert) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
