package com.rogandev.lpp.api

import android.text.Html
import com.rogandev.lpp.ktx.andThen
import retrofit2.Response

class ApiSource internal constructor(private val api: Api) {

    suspend fun getActiveRoutes(): Result<List<ApiRoute>> {
        return runCatching {
            api.activeRoutes()
        }.andThen {
            it.getListData()
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
            it.getListData()
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
        return runCatching {
            api.stationDetails()
        }.andThen {
            it.getListData()
        }
    }

    suspend fun getStation(code: String): Result<ApiStationDetails> {
        return runCatching {
            api.stationDetails(code)
        }.andThen {
            it.getObjectData()
        }
    }

    private fun <T> Response<ApiList<T>>.getListData(): Result<List<T>> = runCatching {
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
