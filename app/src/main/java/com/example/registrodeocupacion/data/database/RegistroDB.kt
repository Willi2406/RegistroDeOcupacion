package com.example.registrodeocupacion.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.registrodeocupacion.data.empleado.local.Converters
import com.example.registrodeocupacion.data.empleado.local.EmpleadoDao
import com.example.registrodeocupacion.data.empleado.local.EmpleadoEntity
import com.example.registrodeocupacion.data.ocupacion.local.OcupacionDao
import com.example.registrodeocupacion.data.ocupacion.local.OcupacioneEntity

@Database(
    entities = [OcupacioneEntity::class, EmpleadoEntity::class],
    version = 2
)
@TypeConverters
    (Converters::class)
abstract class RegistroDB: RoomDatabase() {
    abstract fun OcupacionDao(): OcupacionDao
    abstract fun EmpleadoDao(): EmpleadoDao
}