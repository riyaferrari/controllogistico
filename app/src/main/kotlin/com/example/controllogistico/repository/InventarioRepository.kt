package com.example.controllogistico.repository

import android.content.Context
import com.example.controllogistico.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class InventarioRepository(context: Context) {

    private val materialDao: MaterialDao
    private val proyectoDao: ProyectoDao
    private val solicitudDao: SolicitudDao
    private val databaseSeeder = DatabaseSeeder()


    init {
        val database = AppDatabase.getDatabase(context)
        materialDao = database.materialDao()
        proyectoDao = database.proyectoDao()
        solicitudDao = database.solicitudDao()
    }

    // --- Funciones de Seeding ---
    suspend fun seedDatabaseIfEmpty() {
        val count = materialDao.count()
        if (count == 0) {
            materialDao.insertAll(databaseSeeder.getInitialMaterials())
            proyectoDao.insertAll(databaseSeeder.getInitialProyectos())
        }
    }

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

        // CORRECCIÓN LÓGICA: Primero se aprueba, luego se procesa.
        if (solicitud.estado != EstadoSolicitud.PENDIENTE_APROBACION) return

        // 1. Aprobar la solicitud
        solicitud.estado = EstadoSolicitud.APROBADO
        solicitudDao.updateSolicitud(solicitud)

        // 2. Procesar la lógica de stock
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
                    item.estadoItem = EstadoItemSolicitud.FALTANTE // Marcado como Backorder
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
