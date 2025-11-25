package com.example.controllogistico.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proyectos")
data class Proyecto(
    @PrimaryKey val id: String,
    val nombre: String,
    val lider: String
)
