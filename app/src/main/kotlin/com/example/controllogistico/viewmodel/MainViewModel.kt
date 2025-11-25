package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controllogistico.repository.InventarioRepository

class MainViewModel : ViewModel() {
    private val repository = InventarioRepository()

    val inventarioViewModel: InventarioViewModel by lazy {
        InventarioViewModel(repository)
    }

    val crearSolicitudViewModel: CrearSolicitudViewModel by lazy {
        CrearSolicitudViewModel(repository)
    }

    fun getRepository(): InventarioRepository {
        return repository
    }
}
