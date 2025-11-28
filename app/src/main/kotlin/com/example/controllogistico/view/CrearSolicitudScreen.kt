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
import com.example.controllogistico.model.Material
import com.example.controllogistico.model.Proyecto
import com.example.controllogistico.viewmodel.CrearSolicitudViewModel

/**
 * Pantalla principal para la creación de una nueva solicitud de material.
 * Funciona como un "carrito de compras".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearSolicitudScreen(
    viewModel: CrearSolicitudViewModel,
    onSolicitudEnviada: () -> Unit
) {
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
                onClick = { viewModel.onEnviarSolicitud(onSuccess = onSolicitudEnviada) },
                enabled = carrito.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Solicitud")
            }

            if (showDialog) {
                AgregarMaterialDialog(
                    materiales = materiales.filter { it.stockDisponible > 0 },
                    onDismiss = { showDialog = false },
                    onMaterialAdd = { sku, qty ->
                        viewModel.agregarAlCarrito(sku, qty)
                        showDialog = false
                    }
                )
            }
        }
    }
}

/**
 * Muestra una tarjeta para un ítem dentro del carrito de compras.
 */
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

// --- DEFINICIONES DE COMPOSABLES AUXILIARES ---

/**
 * Composable para mostrar un menú desplegable de selección de proyectos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoSelector(
    proyectos: List<Proyecto>,
    proyectoSeleccionado: Proyecto?,
    onProyectoSelected: (Proyecto) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            value = proyectoSeleccionado?.nombre ?: "Seleccione un Proyecto",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            proyectos.forEach { proyecto ->
                DropdownMenuItem(
                    text = { Text(proyecto.nombre) },
                    onClick = {
                        onProyectoSelected(proyecto)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Diálogo para buscar y agregar un material al carrito de la solicitud.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMaterialDialog(
    materiales: List<Material>,
    onDismiss: () -> Unit,
    onMaterialAdd: (String, Int) -> Unit
) {
    var selectedMaterialSku by remember { mutableStateOf(materiales.firstOrNull()?.sku ?: "") }
    var cantidad by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Material") },
        text = {
            Column {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded}) {
                     TextField(
                        value = materiales.find{it.sku == selectedMaterialSku}?.nombre ?: "Seleccionar material",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        materiales.forEach { material ->
                            DropdownMenuItem(
                                text = { Text(material.nombre) },
                                onClick = {
                                    selectedMaterialSku = material.sku
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val cantidadInt = cantidad.toIntOrNull() ?: 1
                if (selectedMaterialSku.isNotBlank()) {
                    onMaterialAdd(selectedMaterialSku, cantidadInt)
                }
            }) { Text("Agregar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}
