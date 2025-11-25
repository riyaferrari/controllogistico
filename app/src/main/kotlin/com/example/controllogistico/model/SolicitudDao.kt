package com.example.controllogistico.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSolicitud(solicitud: Solicitud)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalles(detalles: List<DetalleSolicitud>)

    @Update
    suspend fun updateSolicitud(solicitud: Solicitud)

    @Update
    suspend fun updateDetalle(detalle: DetalleSolicitud)

    @Transaction
    @Query("SELECT * FROM solicitudes WHERE id = :solicitudId")
    suspend fun getSolicitudConDetalles(solicitudId: String): SolicitudConDetalles

    @Transaction
    @Query("SELECT * FROM solicitudes WHERE estado = :estado")
    fun getSolicitudesPorEstado(estado: EstadoSolicitud): Flow<List<SolicitudConDetalles>>
}
