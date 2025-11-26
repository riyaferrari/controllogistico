package com.example.controllogistico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controllogistico.repository.InventarioRepository
import com.example.controllogistico.view.*
import com.example.controllogistico.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogisticaApp
        setContent {
            ControllogisticoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(repository = app.repository)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(repository: InventarioRepository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(repository))
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToInventario = { navController.navigate("inventario") },
                onNavigateToCrearSolicitud = { navController.navigate("crear_solicitud") },
                onNavigateToProcesarSolicitudes = { navController.navigate("procesar_solicitudes") }
            )
        }
        composable("inventario") {
            val inventarioViewModel: InventarioViewModel = viewModel(factory = ViewModelFactory(repository))
            InventarioScreen(viewModel = inventarioViewModel)
        }
        composable("crear_solicitud") {
            val crearSolicitudViewModel: CrearSolicitudViewModel = viewModel(factory = ViewModelFactory(repository))
            CrearSolicitudScreen(
                viewModel = crearSolicitudViewModel,
                onSolicitudEnviada = { navController.popBackStack() }
            )
        }
        composable("procesar_solicitudes") {
            val procesarSolicitudViewModel: ProcesarSolicitudViewModel = viewModel(factory = ViewModelFactory(repository))
            ProcesarSolicitudScreen(viewModel = procesarSolicitudViewModel)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: InventarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(InventarioViewModel::class.java) -> InventarioViewModel(repository) as T
            modelClass.isAssignableFrom(CrearSolicitudViewModel::class.java) -> CrearSolicitudViewModel(repository) as T
            modelClass.isAssignableFrom(ProcesarSolicitudViewModel::class.java) -> ProcesarSolicitudViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
