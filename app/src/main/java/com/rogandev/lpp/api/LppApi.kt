package com.rogandev.lpp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LppApi {

    companion object {
        const val BASE_URL = "https://data.lpp.si/api/"
    }

    // Route

    @GET("route/active-routes")
    suspend fun activeRoutes(): Response<ApiList<ApiRoute>>

    // Station

    @GET("station/arrival")
    suspend fun stationArrivals(@Query("station-code") code: String): Response<ApiObject<ApiStationArrivals>>

    @GET("station/messages")
    suspend fun stationMessages(@Query("station-code") code: String): Response<ApiList<String>>

    @GET("station/station-details?show-subroutes=1")
    suspend fun stationDetails(): Response<ApiList<ApiStationDetails>>
}
