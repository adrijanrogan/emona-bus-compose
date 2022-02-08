package com.rogandev.lpp.repository

import com.rogandev.lpp.api.ApiSource
import com.rogandev.lpp.ktx.mapList
import com.rogandev.lpp.repository.cache.DbStation
import com.rogandev.lpp.repository.cache.dao.StationDao
import com.rogandev.lpp.repository.cache.meta.Metadata
import com.rogandev.lpp.repository.model.Route
import com.rogandev.lpp.repository.model.Station
import com.rogandev.lpp.repository.model.StationArrival
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiSource: ApiSource,
    private val metadata: Metadata,
    private val stationDao: StationDao,
) {

    suspend fun getActiveRoutes(): Result<List<Route>> {
        return apiSource.getActiveRoutes().mapList {
            Route(
                tripId = it.tripId,
                routeId = it.routeId,
                routeGroup = it.routeNumber,
                tripName = it.routeName
            )
        }
    }

    suspend fun getStationArrivals(code: String): Result<List<StationArrival>> {
        return apiSource.getStationArrivals(code).mapList {
            StationArrival(
                routeGroup = it.routeName,
                tripName = it.tripName,
                eta = it.eta
            )
        }
    }

    suspend fun getStationMessages(code: String): Result<List<String>> {
        return apiSource.getStationMessages(code)
    }

    suspend fun getStations(): Flow<List<Station>> {
        return stationDao.getAll().onStart {
            val lastRefresh = metadata.getStationRefreshTime()
            if (lastRefresh < Instant.now().minus(1, ChronoUnit.HOURS)) {
                val refreshTime = Instant.now()

                apiSource.getStations().mapList {
                    DbStation(it.id, it.name, it.longitude, it.latitude, it.routeGroups.joinToString(separator = ","))
                }.onSuccess { stations ->
                    stationDao.insertAll(stations)
                    metadata.putStationRefreshTime(refreshTime)
                }
            }
        }.map { dbStations ->
            dbStations.map {
                Station(it.code, it.name, it.latitude, it.longitude, it.routeGroups.split(","))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStation(code: String): Result<Station> {
        return apiSource.getStation(code).map {
            Station(it.id, it.name, it.longitude, it.latitude, it.routeGroups)
        }
    }
}
