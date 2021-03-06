package com.rogandev.lpp.repository

import android.content.Context
import androidx.room.Room
import com.rogandev.lpp.repository.cache.Database
import com.rogandev.lpp.repository.cache.dao.StationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, "cache.db").build()
        }

        @Provides
        fun provideStationDao(database: Database): StationDao {
            return database.stationDao()
        }
    }
}
