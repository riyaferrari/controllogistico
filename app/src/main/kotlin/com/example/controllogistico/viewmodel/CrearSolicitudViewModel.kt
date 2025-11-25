package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.model.*
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CrearSolicitudViewModel(private val repository: InventarioRepository) : ViewModel() {

    val proyectos = repository.proyectos
    val materiales = repository.materiales

    private val _proyectoSeleccionado = MutableStateFlow<Proyecto?>(null)
    val proyectoSeleccionado = _proyectoSeleccionado.asStateFlow()

    private val _itemsSolicitud = MutableStateFlow<List<DetalleSolicitud>>(emptyList())
    val itemsSolicitud = _itemsSolicitud.asStateFlow()

    fun onProyectoSeleccionado(proyecto: Proyecto) {
        _proyectoSeleccionado.value = proyecto
    }

    fun onMaterialAgregado(sku: String, cantidad: Int) {
        val nuevoItem = DetalleSolicitud(
            skuMaterial = sku,
            cantidadSolicitada = cantidad,
            cantidadAutorizada = cantidad, // Por defecto, la autorizada es igual a la solicitada
            cantidadSurtida = 0
        )
        _itemsSolicitud.update { currentList ->
            val existingItem = currentList.find { it.skuMaterial == sku }
            if (existingItem != null) {
                // Si el item ya existe, actualiza la cantidad
                currentList.map {
                    if (it.skuMaterial == sku) {
                        it.copy(cantidadSolicitada = it.cantidadSolicitada + cantidad)
                    } else {
                        it
                    }
                }
            } else {
                // Si no existe, lo añade
                currentList + nuevoItem
            }
        }
    }

    fun onEnviarSolicitud() {
        viewModelScope.launch {
            val proyecto = _proyectoSeleccionado.value ?: return@launch
            val items = _itemsSolicitud.value
            if (items.isNotEmpty()) {
                val nuevaSolicitud = SolicitudMaterial(
                    id = "SOL-${System.currentTimeMillis()}",
                    proyectoId = proyecto.id,
                    fecha = System.currentTimeMillis(),
                    estado = EstadoSolicitud.PENDIENTE_APROBACION,
                    items = items
                )
                repository.addSolicitud(nuevaSolicitud)
                // Limpiar formulario después de enviar
                _proyectoSeleccionado.value = null
                _itemsSolicitud.value = emptyList()
            }
        }
    }
}
