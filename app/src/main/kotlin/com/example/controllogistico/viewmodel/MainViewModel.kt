package com.example.controllogistico.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val repository = InventarioRepository(application)

    val homeViewModel: HomeViewModel by lazy {
        HomeViewModel(repository)
    }

    val inventarioViewModel: InventarioViewModel by lazy {
        InventarioViewModel(repository)
    }

    val crearSolicitudViewModel: CrearSolicitudViewModel by lazy {
        CrearSolicitudViewModel(repository)
    }

    val procesarSolicitudViewModel: ProcesarSolicitudViewModel by lazy {
        ProcesarSolicitudViewModel(repository)
    }

    init {
        // Llenar la base de datos con datos iniciales si está vacía
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(application) as T
                }
            }
        }
    }
}
