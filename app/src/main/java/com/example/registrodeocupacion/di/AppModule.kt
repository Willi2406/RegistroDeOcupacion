package com.example.registrodeocupacion.di

import android.content.Context
import androidx.room.Room
import com.example.registrodeocupacion.data.database.RegistroDB
import com.example.registrodeocupacion.data.empleado.local.EmpleadoDao
import com.example.registrodeocupacion.data.empleado.repository.EmpleadoRepositoryImpl
import com.example.registrodeocupacion.data.ocupacion.local.OcupacionDao
import com.example.registrodeocupacion.data.ocupacion.repository.OcupacionRepositoryImpl
import com.example.registrodeocupacion.domain.empleado.repository.EmpleadoRepository
import com.example.registrodeocupacion.domain.ocupacion.repository.OcupacionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(
        ocupacionRepositoryImpl: OcupacionRepositoryImpl
    ): OcupacionRepository

    @Binds
    @Singleton
    abstract fun bindEmpleadoRepository(
        empleadoRepositoryImpl: EmpleadoRepositoryImpl
    ): EmpleadoRepository

    companion object {
        @Provides
        @Singleton
        fun provideOcupacionDB(@ApplicationContext context: Context): RegistroDB {
            return Room.databaseBuilder(
                context,
                RegistroDB::class.java,
                "Ocupacion.db"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun provideOcupacionDao(db: RegistroDB): OcupacionDao {
            return db.OcupacionDao()
        }

        @Provides
        fun provideEmpleadoDao(db: RegistroDB): EmpleadoDao {
            return db.EmpleadoDao()
        }
    }
}
