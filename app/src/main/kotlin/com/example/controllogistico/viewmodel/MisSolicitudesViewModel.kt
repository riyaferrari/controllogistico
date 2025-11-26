package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import com.example.controllogistico.repository.InventarioRepository

class MisSolicitudesViewModel(private val repository: InventarioRepository) : ViewModel() {
    val todasLasSolicitudes = repository.getAllSolicitudes()
}
