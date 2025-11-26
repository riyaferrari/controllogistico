package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controllogistico.repository.InventarioRepository

class DetalleProyectoViewModel(
    repository: InventarioRepository,
    proyectoId: String
) : ViewModel() {

    val proyectoConSolicitudes = repository.getProyectoConSolicitudes(proyectoId)

    companion object {
        fun provideFactory(
            repository: InventarioRepository,
            proyectoId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetalleProyectoViewModel(repository, proyectoId) as T
            }
        }
    }
}
