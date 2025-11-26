package com.example.controllogistico.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

// --- Entidades Principales ---

@Entity(tableName = "proyectos")
data class Proyecto(
    @PrimaryKey val id: String,
    val nombre: String,
    val lider: String
)

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey val id: String,
    val proyectoId: String,
    val fecha: Long,
    var estado: EstadoSolicitud,
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

// --- Clases de Relación para Consultas ---

data class SolicitudConDetalles(
    @Embedded val solicitud: Solicitud,
    @Relation(
        parentColumn = "id",
        entityColumn = "solicitudId"
    )
    val detalles: List<DetalleSolicitud>
)

data class ProyectoConSolicitudes(
    @Embedded val proyecto: Proyecto,
    @Relation(
        parentColumn = "id",
        entityColumn = "proyectoId"
    )
    val solicitudes: List<Solicitud>
)


// --- Enums ---

enum class EstadoSolicitud {
    BORRADOR,
    PENDIENTE_APROBACION,
    APROBADO,
    SURTIDO_PARCIAL,
    COMPLETADO
}

enum class EstadoItemSolicitud {
    PENDIENTE,
    FALTANTE,
    SURTIDO
}
