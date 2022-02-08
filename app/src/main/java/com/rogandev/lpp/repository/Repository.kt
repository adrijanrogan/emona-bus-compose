package com.rogandev.lpp.repository

import android.text.Html
import com.rogandev.lpp.api.*
import com.rogandev.lpp.cache.DbStation
import com.rogandev.lpp.cache.dao.StationDao
import com.rogandev.lpp.cache.meta.MetadataCache
import com.rogandev.lpp.ktx.andThen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: Api,
    private val stationDao: StationDao,
    private val metadataCache: MetadataCache,
) {

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

    suspend fun getStations(): Flow<List<ApiStationDetails>> {
        return stationDao.getAll().onStart {
            val lastRefresh = metadataCache.getStationRefreshTime()
            Timber.d("Last refresh = $lastRefresh")
            if (lastRefresh < Instant.now().minus(1, ChronoUnit.HOURS)) {
                val refreshTime = Instant.now()
                runCatching {
                    api.stationDetails()
                }.andThen {
                    it.getListData()
                }.onSuccess { apiStations ->
                    val dbStations = apiStations.map {
                        DbStation(it.id, it.name, it.longitude, it.latitude, it.routeGroups.joinToString(separator = ","))
                    }
                    stationDao.insertAll(dbStations)
                    metadataCache.putStationRefreshTime(refreshTime)
                }
            }
        }.map { dbStations ->
            dbStations.map {
                ApiStationDetails(it.code, it.name, it.latitude, it.longitude, it.routeGroups.split(","))
            }
        }.flowOn(Dispatchers.IO)
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
