package com.example.registrodeocupacion.data.horasextra.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate



@Entity(tableName = "horasextra")
data class HorasExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horasExtraId: Int = 0,
    val empleadoId: Int = 0,
    val fecha: LocalDate,
    val cantidadHoras: Double,
    val tipo: TipoHoraExtra,
    val recargo: Double
)
