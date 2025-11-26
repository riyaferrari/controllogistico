package com.example.controllogistico

import android.app.Application
import com.example.controllogistico.model.AppDatabase
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LogisticaApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        InventarioRepository(
            database.materialDao(),
            database.proyectoDao(),
            database.solicitudDao()
        )
    }
}
