package com.rogandev.lpp.ui.screen.station

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.BusRepository
import com.rogandev.lpp.ui.model.UiArrival
import com.rogandev.lpp.ui.model.UiRouteGroup
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

    private val _uiStateFlow = MutableStateFlow(StationScreenState(emptyList(), emptyList(),
        loadingMessages = false,
        loadingArrivals = false
    ))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        stationCodeFlow.filter { it.isNotBlank() }.mapLatest { code ->

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

                busRepository.getStationArrivals(code).map { arrivals ->
                    arrivals.groupBy { it.routeName }.toSortedMap(RouteGroupNameComparator).map { (routeName, apiArrivals) ->
                        val routeGroup = UiRouteGroup.fromName(routeName)
                        val etas = apiArrivals.map { "${it.eta.toString(10)} min" }

                        val destination = apiArrivals.first().tripName.takeLastWhile { it != '-' }.lowercase().capitalize(Locale("sl"))

                        UiArrival(routeGroup = routeGroup, destination = destination , etas = etas)
                    }
                }.onSuccess { arrivals ->
                    _uiStateFlow.update { it.copy(arrivals = arrivals, loadingArrivals = false) }
                }.onFailure { err ->
                    Timber.e(err)
                    _uiStateFlow.update { it.copy(loadingArrivals = false) }
                }

                delay(15_000)
            }

        }.launchIn(viewModelScope)
    }

    fun setStationCode(code: String) {
        stationCodeFlow.value = code
    }
}

data class StationScreenState(
    val messages: List<String>,
    val arrivals: List<UiArrival>,
    val loadingMessages: Boolean,
    val loadingArrivals: Boolean,
)
