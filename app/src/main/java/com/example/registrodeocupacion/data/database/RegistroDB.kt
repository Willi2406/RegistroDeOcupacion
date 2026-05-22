package com.example.registrodeocupacion.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.registrodeocupacion.data.empleado.local.Converters
import com.example.registrodeocupacion.data.empleado.local.EmpleadoDao
import com.example.registrodeocupacion.data.empleado.local.EmpleadoEntity
import com.example.registrodeocupacion.data.horasextra.local.HorasExtraConverte
import com.example.registrodeocupacion.data.horasextra.local.HorasExtraDao
import com.example.registrodeocupacion.data.horasextra.local.HorasExtraEntity
import com.example.registrodeocupacion.data.ocupacion.local.OcupacionDao
import com.example.registrodeocupacion.data.ocupacion.local.OcupacioneEntity

@Database(
    entities = [OcupacioneEntity::class, EmpleadoEntity::class, HorasExtraEntity::class],
    version = 3
)
@TypeConverters
    (Converters::class, HorasExtraConverte::class)
abstract class RegistroDB: RoomDatabase() {
    abstract fun OcupacionDao(): OcupacionDao
    abstract fun EmpleadoDao(): EmpleadoDao

    abstract fun HorasExtraDao(): HorasExtraDao
}