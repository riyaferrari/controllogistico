package com.example.controllogistico.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.controllogistico.model.Material
import com.example.controllogistico.model.Proyecto
import com.example.controllogistico.viewmodel.CrearSolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearSolicitudScreen(viewModel: CrearSolicitudViewModel, navController: NavController) {
    val proyectos by viewModel.proyectos.collectAsState()
    val proyectoSeleccionado by viewModel.proyectoSeleccionado.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val materiales by viewModel.materiales.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Nueva Solicitud") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            ProyectoSelector(
                proyectos = proyectos,
                proyectoSeleccionado = proyectoSeleccionado,
                onProyectoSelected = viewModel::onProyectoSeleccionado
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDialog = true }, enabled = proyectoSeleccionado != null) {
                Text("Agregar Material al Carrito")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Carrito de Solicitud:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carrito.keys.toList()) { sku ->
                    val material = materiales.find { it.sku == sku }
                    val cantidad = carrito[sku] ?: 0
                    if (material != null) {
                        CarritoItemCard(
                            material = material,
                            cantidad = cantidad,
                            onDelete = { viewModel.eliminarDelCarrito(sku) },
                            onQuantityChange = { newQty -> viewModel.editarCantidad(sku, newQty) }
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.onEnviarSolicitud { navController.popBackStack() } },
                enabled = carrito.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Solicitud")
            }

            if (showDialog) {
                AgregarMaterialDialog(
                    materiales = materiales.filter { it.stockDisponible > 0 },
                    onDismiss = { showDialog = false },
                    onMaterialAdd = viewModel::agregarAlCarrito
                )
            }
        }
    }
}

@Composable
fun CarritoItemCard(
    material: Material,
    cantidad: Int,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(material.nombre, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = cantidad.toString(),
                onValueChange = { onQuantityChange(it.toIntOrNull() ?: 0) },
                modifier = Modifier.width(80.dp),
                singleLine = true
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
// El resto de composables (ProyectoSelector, AgregarMaterialDialog) se mantienen
