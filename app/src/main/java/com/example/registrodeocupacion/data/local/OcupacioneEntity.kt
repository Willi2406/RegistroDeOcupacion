package com.example.registrodeocupacion.data.local

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ocupaciones")
data class OcupacioneEntity(
    @PrimaryKey(autoGenerate = true)
    val ocupacioneId: Int = 0,
    val descricion: String, val sueldo: Double
)