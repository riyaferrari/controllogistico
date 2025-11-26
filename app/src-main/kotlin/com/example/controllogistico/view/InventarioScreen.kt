package com.example.controllogistico.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.controllogistico.model.Material
import com.example.controllogistico.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(viewModel: InventarioViewModel, navController: NavController) {
    val searchText by viewModel.searchText.collectAsState()
    val materiales by viewModel.materialesVisibles.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("agregar_material") }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Material")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            TextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por Nombre o SKU") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(materiales) { material ->
                    MaterialCard(material = material)
                }
            }
        }
    }
}

@Composable
fun MaterialCard(material: Material) {
    val stockColor = when {
        material.stockDisponible < material.minStock -> MaterialTheme.colorScheme.error
        material.stockDisponible < material.minStock * 1.2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = material.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "SKU: ${material.sku} | Ubic: ${material.ubicacion}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = "${material.stockDisponible}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = stockColor
            )
        }
    }
}
