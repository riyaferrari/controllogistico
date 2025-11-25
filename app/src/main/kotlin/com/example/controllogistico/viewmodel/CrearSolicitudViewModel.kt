package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.model.*
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CrearSolicitudViewModel(private val repository: InventarioRepository) : ViewModel() {

    val proyectos = repository.getProyectos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val materiales = repository.getMateriales().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _proyectoSeleccionado = MutableStateFlow<Proyecto?>(null)
    val proyectoSeleccionado = _proyectoSeleccionado.asStateFlow()

    private val _itemsSolicitud = MutableStateFlow<Map<String, DetalleSolicitud>>(emptyMap())
    val itemsSolicitud = _itemsSolicitud.asStateFlow()

    fun onProyectoSeleccionado(proyecto: Proyecto) {
        _proyectoSeleccionado.value = proyecto
    }

    fun onMaterialAgregado(sku: String, cantidad: Int) {
        _itemsSolicitud.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            val existingItem = newMap[sku]
            if (existingItem != null) {
                newMap[sku] = existingItem.copy(cantidadSolicitada = existingItem.cantidadSolicitada + cantidad)
            } else {
                newMap[sku] = DetalleSolicitud(
                    solicitudId = "", // Se asignará al crear la solicitud
                    skuMaterial = sku,
                    cantidadSolicitada = cantidad,
                    cantidadAutorizada = cantidad,
                    cantidadSurtida = 0
                )
            }
            newMap
        }
    }

    fun onEnviarSolicitud(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val proyecto = _proyectoSeleccionado.value ?: return@launch
            val items = _itemsSolicitud.value.values.toList()
            if (items.isNotEmpty()) {
                val solicitudId = "SOL-${System.currentTimeMillis()}"
                val nuevaSolicitud = Solicitud(
                    id = solicitudId,
                    proyectoId = proyecto.id,
                    fecha = System.currentTimeMillis(),
                    estado = EstadoSolicitud.PENDIENTE_APROBACION,
                )
                val detallesConId = items.map { it.copy(solicitudId = solicitudId) }

                repository.crearNuevaSolicitud(nuevaSolicitud, detallesConId)

                // Limpiar formulario y notificar
                _proyectoSeleccionado.value = null
                _itemsSolicitud.value = emptyMap()
                onSuccess()
            }
        }
    }
}
