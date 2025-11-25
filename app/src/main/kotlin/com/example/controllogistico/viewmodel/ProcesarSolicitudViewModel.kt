package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProcesarSolicitudViewModel(private val repository: InventarioRepository) : ViewModel() {

    val solicitudesPendientes = repository.getSolicitudesPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onProcesarSolicitud(solicitudId: String) {
        viewModelScope.launch {
            repository.procesarSolicitud(solicitudId)
        }
    }
}
