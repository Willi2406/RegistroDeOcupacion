package com.example.registrodeocupacion.data.horasextra.local

import androidx.room.TypeConverter

class HorasExtraConverte {

    @TypeConverter
    fun fromTipoHoraExtra(value: TipoHoraExtra): String {
        return value.name
    }

    @TypeConverter
    fun toTipoHoraExtra(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }
}
