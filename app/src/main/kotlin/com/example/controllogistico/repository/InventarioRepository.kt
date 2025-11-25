package com.example.controllogistico.repository

import com.example.controllogistico.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InventarioRepository {

    // --- Datos Simulados (Mocks) ---
    private val _materiales = MutableStateFlow<List<Material>>(listOf(
        Material("SKU-001", "Fibra Óptica 10m", "Fibra", 100, 10, 20, false),
        Material("SKU-002", "Conector RJ45", "Conectores", 500, 50, 100, true),
        Material("SKU-003", "Router Wifi 6", "Equipos", 50, 5, 10, false),
        Material("SKU-004", "Cable UTP Cat 6", "Cableado", 300, 20, 50, true),
        Material("SKU-005", "Antena Omni", "Antenas", 30, 0, 5, false)
    ))
    val materiales: StateFlow<List<Material>> = _materiales.asStateFlow()

    private val _proyectos = MutableStateFlow<List<Proyecto>>(listOf(
        Proyecto("PROY-A", "Despliegue Urbano Centro", "Ana García"),
        Proyecto("PROY-B", "Conexión Rural Norte", "Carlos Vera")
    ))
    val proyectos: StateFlow<List<Proyecto>> = _proyectos.asStateFlow()

    private val _solicitudes = MutableStateFlow<List<SolicitudMaterial>>(mutableListOf())
    val solicitudes: StateFlow<List<SolicitudMaterial>> = _solicitudes.asStateFlow()

    // --- Lógica de Negocio Crítica ---

    suspend fun addSolicitud(solicitud: SolicitudMaterial) {
        _solicitudes.update { currentList -> currentList + solicitud }
    }

    suspend fun procesarSolicitudLogistica(solicitudId: String) {
        val solicitud = _solicitudes.value.find { it.id == solicitudId } ?: return
        solicitud.estado = EstadoSolicitud.PROCESADO_LOGISTICA

        solicitud.items.forEach { item ->
            val material = _materiales.value.find { it.sku == item.skuMaterial }
            if (material != null) {
                val cantidadAProcesar = item.cantidadAutorizada

                if (material.stockDisponible >= cantidadAProcesar) {
                    // Hay suficiente stock
                    material.stockReservado += cantidadAProcesar
                    item.cantidadSurtida = cantidadAProcesar
                } else {
                    // No hay suficiente, reservar lo que queda
                    val cantidadAReservar = material.stockDisponible
                    material.stockReservado += cantidadAReservar
                    item.cantidadSurtida = cantidadAReservar
                }
            }
        }

        // Forzar la actualización del StateFlow de materiales
        _materiales.update { it.toList() }
    }
}
