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

    // Carrito Temporal
    private val _carrito = MutableStateFlow<Map<String, Int>>(emptyMap()) // Map<SKU, Cantidad>
    val carrito: StateFlow<Map<String, Int>> = _carrito.asStateFlow()

    fun onProyectoSeleccionado(proyecto: Proyecto) {
        _proyectoSeleccionado.value = proyecto
    }

    fun agregarAlCarrito(sku: String, cantidad: Int) {
        _carrito.update { currentCart ->
            val newCart = currentCart.toMutableMap()
            newCart[sku] = (newCart[sku] ?: 0) + cantidad
            newCart
        }
    }

    fun editarCantidad(sku: String, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDelCarrito(sku)
        } else {
            _carrito.update { currentCart ->
                val newCart = currentCart.toMutableMap()
                newCart[sku] = nuevaCantidad
                newCart
            }
        }
    }

    fun eliminarDelCarrito(sku: String) {
        _carrito.update { currentCart ->
            val newCart = currentCart.toMutableMap()
            newCart.remove(sku)
            newCart
        }
    }

    fun onEnviarSolicitud(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val proyecto = _proyectoSeleccionado.value ?: return@launch
            val itemsCarrito = _carrito.value
            if (itemsCarrito.isNotEmpty()) {
                val solicitudId = "SOL-${System.currentTimeMillis()}"
                val nuevaSolicitud = Solicitud(
                    id = solicitudId,
                    proyectoId = proyecto.id,
                    fecha = System.currentTimeMillis(),
                    estado = EstadoSolicitud.PENDIENTE_APROBACION,
                )
                val detalles = itemsCarrito.map { (sku, cantidad) ->
                    DetalleSolicitud(
                        solicitudId = solicitudId,
                        skuMaterial = sku,
                        cantidadSolicitada = cantidad,
                        cantidadAutorizada = cantidad, // Por defecto, se autoriza lo mismo
                        cantidadSurtida = 0
                    )
                }

                repository.crearNuevaSolicitud(nuevaSolicitud, detalles)

                // Limpiar y notificar
                _proyectoSeleccionado.value = null
                _carrito.value = emptyMap()
                onSuccess()
            }
        }
    }
}
