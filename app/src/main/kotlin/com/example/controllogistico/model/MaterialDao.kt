package com.example.controllogistico.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
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
