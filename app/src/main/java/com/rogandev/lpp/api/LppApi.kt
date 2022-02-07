package com.rogandev.lpp.api

import retrofit2.Response
import retrofit2.http.GET

interface LppApi {

    companion object {
        const val BASE_URL = "https://data.lpp.si/api/"
    }

    // Station

    @GET("station/station-details?show-subroutes=1")
    suspend fun stationDetails(): Response<LppList<StationDetails>>
}
