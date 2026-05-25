package com.example.registrodeocupacion.data.empleado.local

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
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
    fun fromFrecuenciaPago(value: FrecuenciaPago): String {
        return value.name
    }

    @TypeConverter
    fun toFrecuenciaPago(value: String): FrecuenciaPago {
        return FrecuenciaPago.valueOf(value)
    }
}
