package com.example.controllogistico.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.controllogistico.model.Material
import com.example.controllogistico.viewmodel.InventarioViewModel

@Composable
fun InventarioScreen(viewModel: InventarioViewModel) {
    val searchText by viewModel.searchText.collectAsState()
    val materiales by viewModel.materialesVisibles.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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

@Composable
fun MaterialCard(material: Material) {
    val stockColor = when {
        material.stockDisponible < material.minStock -> Color.Red
        material.stockDisponible < material.minStock * 1.2 -> Color(0xFFFFA500) // Orange
        else -> Color.Green
    }

    Card(
        modifier = Modifier.fillMaxWidth().border(2.dp, stockColor, CardDefaults.shape),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = material.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "SKU: ${material.sku} | Ubic: ${material.ubicacion}", fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = "${material.stockDisponible}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = stockColor
            )
        }
    }
}
