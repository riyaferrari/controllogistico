package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.model.Material
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.launch

class AgregarMaterialViewModel(private val repository: InventarioRepository) : ViewModel() {

    fun agregarMaterial(
        sku: String,
        nombre: String,
        categoria: String,
        stockInicial: Int,
        minStock: Int,
        precio: Double,
        ubicacion: String,
        esConsumible: Boolean,
        onSuccess: () -> Unit
    ) {
        if (sku.isBlank() || nombre.isBlank() || categoria.isBlank()) {
            return
        }

        viewModelScope.launch {
            val nuevoMaterial = Material(
                sku = sku,
                nombre = nombre,
                categoria = categoria,
                stockFisico = stockInicial,
                stockReservado = 0,
                minStock = minStock,
                precioPromedio = precio,
                ubicacion = ubicacion,
                esConsumible = esConsumible
            )
            // CORRECCIÓN: Llamar a la función del repositorio.
            repository.agregarMaterial(nuevoMaterial)
            onSuccess()
        }
    }
}
