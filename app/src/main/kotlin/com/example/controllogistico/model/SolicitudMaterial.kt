package com.example.controllogistico.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey val id: String,
    val proyectoId: String,
    val fecha: Long,
    var estado: EstadoSolicitud,
)

data class SolicitudConDetalles(
    @Embedded val solicitud: Solicitud,
    @Relation(
        parentColumn = "id",
        entityColumn = "solicitudId"
    )
    val detalles: List<DetalleSolicitud>
)

@Entity(tableName = "detalles_solicitud")
data class DetalleSolicitud(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val solicitudId: String,
    val skuMaterial: String,
    val cantidadSolicitada: Int,
    var cantidadAutorizada: Int,
    var cantidadSurtida: Int,
    var estadoItem: EstadoItemSolicitud = EstadoItemSolicitud.PENDIENTE
)

enum class EstadoSolicitud {
    BORRADOR,
    PENDIENTE_APROBACION,
    APROBADO,
    SURTIDO_PARCIAL,
    COMPLETADO
}

enum class EstadoItemSolicitud {
    PENDIENTE,
    FALTANTE, // Backorder
    SURTIDO
}
