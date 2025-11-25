package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class KpiUiState(
    val valorInventario: Double = 0.0,
    val solicitudesPendientes: Int = 0,
    val alertasStockBajo: Int = 0
)

class HomeViewModel(private val repository: InventarioRepository) : ViewModel() {

    val kpiState: StateFlow<KpiUiState> =
        repository.getMateriales().combine(repository.getSolicitudesPendientes()) { materiales, solicitudes ->
            KpiUiState(
                valorInventario = materiales.sumOf { it.stockFisico * it.precioPromedio },
                solicitudesPendientes = solicitudes.size,
                alertasStockBajo = materiales.count { it.stockDisponible < it.minStock }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KpiUiState()
        )
}
