package com.example.controllogistico.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materiales")
data class Material(
    @PrimaryKey val sku: String,
    val nombre: String,
    val categoria: String,
    var stockFisico: Int,
    var stockReservado: Int,
    val minStock: Int,
    val precioPromedio: Double,
    val ubicacion: String,
    val esConsumible: Boolean
) {
    val stockDisponible: Int
        get() = stockFisico - stockReservado
}
