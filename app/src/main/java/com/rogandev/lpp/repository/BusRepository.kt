package com.rogandev.lpp.repository

import android.text.Html
import com.rogandev.lpp.api.*
import com.rogandev.lpp.ktx.andThen
import retrofit2.Response
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusRepository @Inject constructor(private val api: LppApi) {

    private var stops = listOf<ApiStationDetails>()
    private var stopsRefresh = Instant.MIN

    suspend fun getActiveRoutes(): Result<List<ApiRoute>> {
        return runCatching {
            api.activeRoutes()
        }.andThen {
            it.getData()
        }
    }

    suspend fun getStationArrivals(code: String): Result<List<ApiStationArrival>> {
        return runCatching {
            api.stationArrivals(code)
        }.andThen {
            it.getObjectData()
        }.map {
            it.arrivals
        }
    }

    suspend fun getStationMessages(code: String): Result<List<String>> {
        return runCatching {
            api.stationMessages(code)
        }.andThen {
            it.getData()
        }.map { apiMessages ->
            apiMessages.flatMap { apiMessage ->
                val ampersanded = apiMessage.replace("%26", "&")
                Html.fromHtml(ampersanded, Html.FROM_HTML_MODE_LEGACY).split("***").flatMap { unescaped ->
                    unescaped.lines().map { it.trim() }.filter { it.isNotBlank() }
                }
            }
        }
    }

    suspend fun getStations(): Result<List<ApiStationDetails>> {
        return if (stopsRefresh.plus(5, ChronoUnit.MINUTES).isAfter(Instant.now())) {
            Result.success(stops)
        } else {
            runCatching {
                api.stationDetails()
            }.andThen {
                it.getData()
            }.onSuccess {
                stops = it
                stopsRefresh = Instant.now()
            }
        }
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

    private fun <T> Response<ApiObject<T>>.getObjectData(): Result<T> = runCatching {
        val body = body()

        if (isSuccessful.not()) {
            throw Throwable("Response not successful, error body: ${errorBody()}")
        }

        if (body == null) {
            throw Throwable("Response is successful, but body is null")
        }

        body.data ?: throw Throwable("Response and body is successful, but data is null")
    }
}
