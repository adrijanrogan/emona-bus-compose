package com.rogandev.lpp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        fun provideApiSource(): ApiSource {
            val json = Json {
                ignoreUnknownKeys = true
            }
            val contentType = MediaType.get("application/json")

            val retrofit = Retrofit.Builder()
                .baseUrl(com.rogandev.lpp.api.Api.BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()

            val api = retrofit.create<Api>()

            return ApiSource(api)
        }
    }
}
