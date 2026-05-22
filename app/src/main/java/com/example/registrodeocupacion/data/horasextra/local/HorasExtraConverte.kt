package com.example.registrodeocupacion.data.horasextra.local

import androidx.room.TypeConverter
import com.example.registrodeocupacion.data.empleado.local.FrecuenciaPago
import java.time.LocalDate

class HorasExtraConverte {

    @TypeConverter
    fun fromString(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun toString(date: LocalDate?): String?
    {
        return date?.toString()
    }

    @TypeConverter
    fun fromTipoHoraExtra(value: FrecuenciaPago): String {
        return value.name
    }

    @TypeConverter
    fun toTipoHoraExtra(value: String): FrecuenciaPago {
        return FrecuenciaPago.valueOf(value)
    }
}