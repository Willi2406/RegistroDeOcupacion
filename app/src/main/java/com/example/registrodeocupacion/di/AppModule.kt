package com.example.registrodeocupacion.di

import android.content.Context
import androidx.room.Room
import com.example.registrodeocupacion.data.database.OcupacionDB
import com.example.registrodeocupacion.data.local.OcupacionDao
import com.example.registrodeocupacion.data.repository.OcupacionRepositoryImpl
import com.example.registrodeocupacion.domain.repository.OcupacionRepository
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

    companion object {
        @Provides
        @Singleton
        fun provideOcupacionDB(@ApplicationContext context: Context): OcupacionDB {
            return Room.databaseBuilder(
                context,
                OcupacionDB::class.java,
                "Ocupacion.db"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun provideOcupacionDao(db: OcupacionDB): OcupacionDao {
            return db.OcupacionDao()
        }
    }
}
