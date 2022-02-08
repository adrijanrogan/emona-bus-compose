package com.rogandev.lpp.repository

import com.rogandev.lpp.api.ApiList
import com.rogandev.lpp.api.ApiRoute
import com.rogandev.lpp.api.ApiStationDetails
import com.rogandev.lpp.api.LppApi
import retrofit2.Response
import javax.inject.Inject

class BusRepository @Inject constructor(private val api: LppApi) {

    suspend fun getActiveRoutes(): Result<List<ApiRoute>> {
        return api.activeRoutes().getData()
    }

    suspend fun getStations(): Result<List<ApiStationDetails>> {
        return api.stationDetails().getData()
    }

    private fun <T> Response<ApiList<T>>.getData(): Result<List<T>> = runCatching {
        val body = body()

        if (isSuccessful.not()) {
            throw Throwable("Response not successful, error body: ${errorBody()}")
        }

        if (body == null) {
            throw Throwable("Response is successful, but body is null")
        }

        if (body.success.not()) {
            throw Throwable("Response is successful, but body says failure")
        }

        body.data ?: throw Throwable("Response and body is successful, but data is null")
    }
}
