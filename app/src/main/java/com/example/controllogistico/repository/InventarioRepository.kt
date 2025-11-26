package com.example.controllogistico.repository

import com.example.controllogistico.model.*
import kotlinx.coroutines.flow.Flow

class InventarioRepository(
    private val materialDao: MaterialDao,
    private val proyectoDao: ProyectoDao,
    private val solicitudDao: SolicitudDao
) {

    // --- Flujos de Datos ---
    fun getMateriales(): Flow<List<Material>> = materialDao.getAll()
    fun getProyectos(): Flow<List<Proyecto>> = proyectoDao.getAll()
    fun getSolicitudesPendientes(): Flow<List<SolicitudConDetalles>> =
        solicitudDao.getSolicitudesPorEstado(EstadoSolicitud.PENDIENTE_APROBACION)


    // --- Lógica de Negocio Crítica ("Smart Stock") ---
    suspend fun crearNuevaSolicitud(solicitud: Solicitud, detalles: List<DetalleSolicitud>) {
        solicitudDao.insertSolicitud(solicitud)
        solicitudDao.insertDetalles(detalles)
    }

    suspend fun procesarSolicitud(solicitudId: String) {
        val solicitudConDetalles = solicitudDao.getSolicitudConDetalles(solicitudId)
        val solicitud = solicitudConDetalles.solicitud

        if (solicitud.estado != EstadoSolicitud.PENDIENTE_APROBACION) return

        solicitud.estado = EstadoSolicitud.APROBADO
        solicitudDao.updateSolicitud(solicitud)

        var surtidoCompleto = true

        for (item in solicitudConDetalles.detalles) {
            val material = materialDao.getBySku(item.skuMaterial)
            if (material != null) {
                val cantidadAProcesar = item.cantidadAutorizada

                if (material.stockDisponible >= cantidadAProcesar) {
                    material.stockReservado += cantidadAProcesar
                    item.cantidadSurtida = cantidadAProcesar
                    item.estadoItem = EstadoItemSolicitud.SURTIDO
                } else {
                    val cantidadDisponible = material.stockDisponible
                    material.stockReservado += cantidadDisponible
                    item.cantidadSurtida = cantidadDisponible
                    item.estadoItem = EstadoItemSolicitud.FALTANTE
                    surtidoCompleto = false
                }
                materialDao.update(material)
                solicitudDao.updateDetalle(item)
            }
        }

        solicitud.estado = if (surtidoCompleto) EstadoSolicitud.COMPLETADO else EstadoSolicitud.SURTIDO_PARCIAL
        solicitudDao.updateSolicitud(solicitud)
    }
}
