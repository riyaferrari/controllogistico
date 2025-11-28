package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de procesamiento de solicitudes.
 * Se encarga de obtener las solicitudes pendientes y de ejecutar la lógica de negocio para procesarlas.
 */
class ProcesarSolicitudViewModel(private val repository: InventarioRepository) : ViewModel() {

    val solicitudesPendientes = repository.getSolicitudesPendientes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun procesarSolicitud(solicitudId: String) {
        viewModelScope.launch {
            repository.procesarSolicitud(solicitudId)
        }
    }
}
