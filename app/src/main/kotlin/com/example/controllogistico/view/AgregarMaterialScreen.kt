package com.example.controllogistico.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controllogistico.viewmodel.AgregarMaterialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMaterialScreen(
    viewModel: AgregarMaterialViewModel,
    onBack: () -> Unit
) {
    var sku by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var stockInicial by remember { mutableStateOf("0") }
    var minStock by remember { mutableStateOf("0") }
    var precio by remember { mutableStateOf("0.0") }
    var ubicacion by remember { mutableStateOf("") }
    var esConsumible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Nuevo Material") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU") })
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
            OutlinedTextField(value = stockInicial, onValueChange = { stockInicial = it }, label = { Text("Stock Inicial") })
            OutlinedTextField(value = minStock, onValueChange = { minStock = it }, label = { Text("Stock Mínimo") })
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio Promedio") })
            OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") })
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = esConsumible, onCheckedChange = { esConsumible = it })
                Text("Es Consumible")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.agregarMaterial(
                        sku = sku,
                        nombre = nombre,
                        categoria = categoria,
                        stockInicial = stockInicial.toIntOrNull() ?: 0,
                        minStock = minStock.toIntOrNull() ?: 0,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        ubicacion = ubicacion,
                        esConsumible = esConsumible,
                        onSuccess = onBack
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Material")
            }
        }
    }
}
