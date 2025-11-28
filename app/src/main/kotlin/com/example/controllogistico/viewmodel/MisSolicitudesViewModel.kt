package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import com.example.controllogistico.repository.InventarioRepository

class MisSolicitudesViewModel(private val repository: InventarioRepository) : ViewModel() {
    // CORRECCIÓN: Llamar al método correcto del repositorio.
    val todasLasSolicitudes = repository.getAllSolicitudesConDetalles()
}
