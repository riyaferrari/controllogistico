package com.example.controllogistico.repository

import android.content.Context
import com.example.controllogistico.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class InventarioRepository(private val context: Context) {

    private val materialDao: MaterialDao
    private val proyectoDao: ProyectoDao
    private val solicitudDao: SolicitudDao

    init {
        val database = AppDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO))
        materialDao = database.materialDao()
        proyectoDao = database.proyectoDao()
        solicitudDao = database.solicitudDao()
    }

    // --- Flujos de Datos ---
    fun getMateriales(): Flow<List<Material>> = materialDao.getAll()
    fun getProyectos(): Flow<List<Proyecto>> = proyectoDao.getAll()
    fun getProyectoConSolicitudes(proyectoId: String): Flow<ProyectoConSolicitudes> =
        proyectoDao.getProyectoConSolicitudes(proyectoId)
    fun getAllSolicitudes(): Flow<List<SolicitudConDetalles>> = solicitudDao.getAllSolicitudesConDetalles()
    fun getSolicitudesPendientes(): Flow<List<SolicitudConDetalles>> =
        solicitudDao.getSolicitudesPorEstado(EstadoSolicitud.PENDIENTE_APROBACION)

    // --- Lógica de Negocio ---
    suspend fun agregarMaterial(material: Material) {
        materialDao.insert(material)
    }

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
