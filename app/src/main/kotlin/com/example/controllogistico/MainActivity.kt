package com.example.controllogistico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.controllogistico.repository.InventarioRepository
import com.example.controllogistico.view.*
import com.example.controllogistico.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var repository: InventarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = InventarioRepository(applicationContext)

        setContent {
            ControllogisticoTheme {
                AppShell(repository = repository)
            }
        }
    }
}

@Composable
fun AppShell(repository: InventarioRepository) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(repository)

    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Dashboard.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoute.Dashboard.path) {
                val homeViewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(viewModel = homeViewModel)
            }
            composable(NavRoute.Inventario.path) {
                val inventarioViewModel: InventarioViewModel = viewModel(factory = factory)
                InventarioScreen(viewModel = inventarioViewModel, navController = navController)
            }
            composable(NavRoute.MisSolicitudes.path) {
                // ...
            }
            composable(NavRoute.Despacho.path) {
                val procesarSolicitudViewModel: ProcesarSolicitudViewModel = viewModel(factory = factory)
                ProcesarSolicitudScreen(viewModel = procesarSolicitudViewModel, navController = navController)
            }
            // Agrega aquí las otras rutas que no están en la barra inferior
            composable("agregar_material") {
                 val agregarMaterialViewModel: AgregarMaterialViewModel = viewModel(factory = factory)
                 AgregarMaterialScreen(viewModel = agregarMaterialViewModel, navController = navController)
            }
             composable("crear_solicitud") {
                val crearSolicitudViewModel: CrearSolicitudViewModel = viewModel(factory = factory)
                CrearSolicitudScreen(viewModel = crearSolicitudViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        NavRoute.Dashboard,
        NavRoute.Inventario,
        NavRoute.MisSolicitudes,
        NavRoute.Despacho
    )

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.path,
                onClick = {
                    navController.navigate(item.path) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

sealed class NavRoute(val path: String, val title: String, val icon: ImageVector) {
    object Dashboard : NavRoute("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Inventario : NavRoute("inventario", "Inventario", Icons.Filled.Inventory)
    object MisSolicitudes : NavRoute("mis_solicitudes", "Mis Solicitudes", Icons.Filled.ListAlt)
    object Despacho : NavRoute("despacho", "Despacho", Icons.Filled.LocalShipping)
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: InventarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(InventarioViewModel::class.java) -> InventarioViewModel(repository) as T
            modelClass.isAssignableFrom(CrearSolicitudViewModel::class.java) -> CrearSolicitudViewModel(repository) as T
            modelClass.isAssignableFrom(ProcesarSolicitudViewModel::class.java) -> ProcesarSolicitudViewModel(repository) as T
            modelClass.isAssignableFrom(AgregarMaterialViewModel::class.java) -> AgregarMaterialViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
