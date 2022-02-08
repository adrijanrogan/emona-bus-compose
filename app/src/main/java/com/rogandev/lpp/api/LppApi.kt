package com.rogandev.lpp.api

import retrofit2.Response
import retrofit2.http.GET

interface LppApi {

    companion object {
        const val BASE_URL = "https://data.lpp.si/api/"
    }

    // Route

    @GET("route/active-routes")
    suspend fun activeRoutes(): Response<ApiList<ApiRoute>>

    // Station

    @GET("station/station-details?show-subroutes=1")
    suspend fun stationDetails(): Response<ApiList<ApiStationDetails>>
}
