package com.example.controllogistico.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(material: Material)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(materiales: List<Material>)

    @Update
    suspend fun update(material: Material)

    @Query("SELECT * FROM materiales")
    fun getAll(): Flow<List<Material>>

    @Query("SELECT * FROM materiales WHERE sku = :sku")
    suspend fun getBySku(sku: String): Material?

    @Query("SELECT COUNT(*) FROM materiales")
    suspend fun count(): Int
}

@Dao
interface ProyectoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(proyectos: List<Proyecto>)

    @Query("SELECT * FROM proyectos")
    fun getAll(): Flow<List<Proyecto>>

    @Transaction
    @Query("SELECT * FROM proyectos WHERE id = :proyectoId")
    fun getProyectoConSolicitudes(proyectoId: String): Flow<ProyectoConSolicitudes>
}

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
    @Query("SELECT * FROM solicitudes")
    fun getAllSolicitudesConDetalles(): Flow<List<SolicitudConDetalles>>

    @Transaction
    @Query("SELECT * FROM solicitudes WHERE id = :solicitudId")
    suspend fun getSolicitudConDetalles(solicitudId: String): SolicitudConDetalles

    @Transaction
    @Query("SELECT * FROM solicitudes WHERE estado = :estado")
    fun getSolicitudesPorEstado(estado: EstadoSolicitud): Flow<List<SolicitudConDetalles>>
}
