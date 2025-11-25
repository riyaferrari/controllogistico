package com.example.controllogistico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controllogistico.ui.theme.ControllogisticoTheme
import com.example.controllogistico.view.CrearSolicitudScreen
import com.example.controllogistico.view.HomeScreen
import com.example.controllogistico.view.InventarioScreen
import com.example.controllogistico.view.ProcesarSolicitudScreen
import com.example.controllogistico.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControllogisticoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = mainViewModel.homeViewModel,
                onNavigateToInventario = { navController.navigate("inventario") },
                onNavigateToCrearSolicitud = { navController.navigate("crear_solicitud") },
                onNavigateToProcesarSolicitudes = { navController.navigate("procesar_solicitudes") }
            )
        }
        composable("inventario") {
            InventarioScreen(viewModel = mainViewModel.inventarioViewModel)
        }
        composable("crear_solicitud") {
             CrearSolicitudScreen(
                viewModel = mainViewModel.crearSolicitudViewModel,
                onSolicitudEnviada = { navController.popBackStack() }
            )
        }
        composable("procesar_solicitudes") {
            ProcesarSolicitudScreen(viewModel = mainViewModel.procesarSolicitudViewModel)
        }
    }
}
