package com.example.controllogistico.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProyectoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(proyectos: List<Proyecto>)

    @Query("SELECT * FROM proyectos")
    fun getAll(): Flow<List<Proyecto>>
}
