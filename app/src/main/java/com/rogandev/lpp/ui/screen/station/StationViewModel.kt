package com.rogandev.lpp.ui.screen.station

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.BusRepository
import com.rogandev.lpp.ui.model.UiArrival
import com.rogandev.lpp.ui.model.UiArrivalGroup
import com.rogandev.lpp.ui.model.UiRouteGroup
import com.rogandev.lpp.ui.model.UiStation
import com.rogandev.lpp.ui.screen.routes.RouteGroupNameComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(
    private val busRepository: BusRepository,
) : ViewModel() {

    private val stationCodeFlow = MutableStateFlow("")

    private val _uiStateFlow = MutableStateFlow(StationScreenState(
        station = null,
        messages = emptyList(),
        arrivalGroups = emptyList(),
        loadingMessages = false,
        loadingArrivals = false,
        loadingStation = false,
    ))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        stationCodeFlow.filter { it.isNotBlank() }.mapLatest { code ->

            _uiStateFlow.update { it.copy(loadingArrivals = true, loadingStation = true, loadingMessages = true) }

            // Fetch station details once
            busRepository.getStation(code).map {
                val routeGroups = it.routeGroups.map { groupName ->
                    UiRouteGroup.fromName(groupName)
                }
                UiStation(it.id, it.name, it.latitude, it.longitude, routeGroups)
            }.onSuccess { station ->
                _uiStateFlow.update { it.copy(station = station, loadingStation = false) }
            }.onFailure { err ->
                Timber.e(err)
                _uiStateFlow.update { it.copy(loadingStation = false) }
            }

            // Fetch messages once
            busRepository.getStationMessages(code).onSuccess { messages ->
                _uiStateFlow.update { it.copy(messages = messages, loadingMessages = false) }
            }.onFailure { err ->
                Timber.e(err)
                _uiStateFlow.update { it.copy(loadingMessages = false) }
            }

            // Fetch arrivals every 15 seconds

            while (true) {
                _uiStateFlow.update { it.copy(loadingArrivals = true) }

                busRepository.getStationArrivals(code).map { apiArrivals ->
                    apiArrivals.groupBy { it.routeName }.toSortedMap(RouteGroupNameComparator).map { (routeName, apiArrivalsOnRoute) ->
                        val routeGroup = UiRouteGroup.fromName(routeName)
                        val arrivals = apiArrivalsOnRoute.map {
                            UiArrival("${it.eta.toString(10)} min", false)
                        }

                        val destination = apiArrivalsOnRoute.first().tripName.takeLastWhile { it != '-' }.trim().lowercase().capitalize(Locale("sl"))

                        UiArrivalGroup(routeGroup = routeGroup, destination = destination , arrivals = arrivals)
                    }
                }.onSuccess { arrivals ->
                    _uiStateFlow.update { it.copy(arrivalGroups = arrivals, loadingArrivals = false) }
                }.onFailure { err ->
                    Timber.e(err)
                    _uiStateFlow.update { it.copy(loadingArrivals = false) }
                }

                delay(30_000)
            }

        }.launchIn(viewModelScope)
    }

    fun setStationCode(code: String) {
        stationCodeFlow.value = code
    }
}

data class StationScreenState(
    val station: UiStation?,
    val messages: List<String>,
    val arrivalGroups: List<UiArrivalGroup>,
    val loadingStation: Boolean,
    val loadingMessages: Boolean,
    val loadingArrivals: Boolean,
)
