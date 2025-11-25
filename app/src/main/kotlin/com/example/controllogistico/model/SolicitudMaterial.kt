package com.example.controllogistico.model

data class SolicitudMaterial(
    val id: String,
    val proyectoId: String,
    val fecha: Long,
    var estado: EstadoSolicitud,
    val items: List<DetalleSolicitud>
)

data class DetalleSolicitud(
    val skuMaterial: String,
    val cantidadSolicitada: Int,
    var cantidadAutorizada: Int,
    var cantidadSurtida: Int
)

enum class EstadoSolicitud {
    BORRADOR,
    PENDIENTE_APROBACION,
    APROBADO_GERENCIA,
    PROCESADO_LOGISTICA,
    ENTREGADO
}
