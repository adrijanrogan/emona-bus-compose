package com.rogandev.lpp.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    companion object {

        @Provides
        @Singleton
        fun provideApi(@ApplicationContext context: Context): LppApi {
            val json = Json {
                ignoreUnknownKeys = true
            }
            val contentType = MediaType.get("application/json")

            val retrofit = Retrofit.Builder()
                .baseUrl(LppApi.BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()

            return retrofit.create()
        }
    }
}
