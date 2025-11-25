package com.example.controllogistico.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Material::class, Proyecto::class, Solicitud::class, DetalleSolicitud::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materialDao(): MaterialDao
    abstract fun proyectoDao(): ProyectoDao
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "control_logistico_database"
                )
                .addCallback(DatabaseSeeder())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

private class DatabaseSeeder : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Usamos un CoroutineScope para lanzar la operación de seeder en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            // Aquí iría la lógica para obtener la instancia de la base de datos y los DAOs
            // Sin embargo, como el callback no tiene acceso directo a la instancia de la DB,
            // esta lógica se manejaría mejor en la inicialización, fuera del callback.
            // Para este MVP, dejaremos el seeder como una función a llamar desde el ViewModel principal.
        }
    }

    fun getInitialMaterials(): List<Material> {
        return listOf(
            Material("SKU-001", "Fibra Óptica 10m", "Fibra", 100, 10, 20, 150.75, "A1-1", false),
            Material("SKU-002", "Conector RJ45", "Conectores", 500, 50, 100, 1.25, "B2-3", true),
            Material("SKU-003", "Router Wifi 6", "Equipos", 50, 5, 10, 89.99, "C1-5", false),
            Material("SKU-004", "Cable UTP Cat 6", "Cableado", 300, 20, 50, 0.80, "A1-2", true),
            Material("SKU-005", "Antena Omni", "Antenas", 30, 0, 5, 250.0, "D3-1", false),
            Material("SKU-006", "Tubo Conduit 1/2", "Tubería", 200, 0, 40, 5.50, "E1-1", false),
            Material("SKU-007", "Caja de Registro", "Cajas", 150, 10, 30, 12.0, "E1-2", false),
            Material("SKU-008", "Cinchos de Plástico", "Fijación", 1000, 100, 200, 0.10, "B2-4", true),
            Material("SKU-009", "Taquetes 1/4", "Fijación", 800, 50, 150, 0.05, "B2-5", true),
            Material("SKU-010", "Switch 8 Puertos", "Equipos", 40, 5, 8, 45.50, "C1-6", false),
            // ... agregar 10 materiales más
            Material("SKU-011", "Grapas para Cable", "Fijación", 2000, 200, 500, 0.02, "B2-6", true),
            Material("SKU-012", "Servidor Básico", "Equipos", 10, 1, 3, 1200.0, "C2-1", false),
            Material("SKU-013", "Patch Panel 24p", "Cableado", 25, 3, 5, 75.0, "A2-1", false),
            Material("SKU-014", "Rack de Pared", "Racks", 15, 2, 4, 350.0, "D1-1", false),
            Material("SKU-015", "Tornillos para Rack", "Fijación", 500, 50, 100, 0.25, "B3-1", true),
            Material("SKU-016", "UPS 1500VA", "Energía", 20, 2, 5, 450.0, "C2-2", false),
            Material("SKU-017", "Placa de Pared RJ45", "Conectores", 300, 30, 60, 2.50, "B3-2", false),
            Material("SKU-018", "Multímetro Digital", "Herramienta", 5, 1, 2, 120.0, "F1-1", false),
            Material("SKU-019", "Escalera de Extensión", "Herramienta", 3, 0, 1, 300.0, "F1-2", false),
            Material("SKU-020", "Bobina UTP 305m", "Cableado", 10, 1, 3, 150.0, "A1-3", false)
        )
    }

    fun getInitialProyectos(): List<Proyecto> {
        return listOf(
            Proyecto("PROY-A", "Despliegue Urbano Centro", "Ana García"),
            Proyecto("PROY-B", "Conexión Rural Norte", "Carlos Vera")
        )
    }
}
