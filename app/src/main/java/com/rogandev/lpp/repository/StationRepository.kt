package com.rogandev.lpp.repository

import com.rogandev.lpp.api.LppApi
import javax.inject.Inject

class StationRepository @Inject constructor(private val api: LppApi) {

    suspend fun getStations(): Result<List<Station>> {
        return runCatching {
            val response = api.stationDetails()
            val body = response.body()

            if (response.isSuccessful.not()) {
                throw Throwable("Response not successful, error body: ${response.errorBody()}")
            }

            if (body == null) {
                throw Throwable("Response is successful, but body is null")
            }

            if (body.success.not()) {
                throw Throwable("Response is successful, but body says failure")
            }

            body.data ?: throw Throwable("Response and body is successful, but data is null")
        }.map { list ->
            list.map {
                Station(
                    id = it.id,
                    name = it.name,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    routeGroups = it.routes
                )
            }
        }
    }
}
