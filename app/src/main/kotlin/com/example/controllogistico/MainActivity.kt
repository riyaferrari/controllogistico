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
import com.example.controllogistico.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

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
                repository = mainViewModel.getRepository(),
                onNavigateToInventario = { navController.navigate("inventario") },
                onNavigateToNuevaSolicitud = { navController.navigate("nueva_solicitud") }
            )
        }
        composable("inventario") {
            InventarioScreen(viewModel = mainViewModel.inventarioViewModel)
        }
        composable("nueva_solicitud") {
            CrearSolicitudScreen(viewModel = mainViewModel.crearSolicitudViewModel)
        }
    }
}
