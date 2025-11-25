package com.example.controllogistico.model

data class Material(
    val sku: String,
    val nombre: String,
    val categoria: String,
    var stockFisico: Int,
    var stockReservado: Int,
    val minStock: Int,
    val esConsumible: Boolean
) {
    val stockDisponible: Int
        get() = stockFisico - stockReservado
}
