package com.rogandev.lpp.app

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.api.LppApi
import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val api: LppApi
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(StationsState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                _uiStateFlow.update { it.copy(loading = true) }
                delay(1000)
                api.stationDetails().body() ?: throw NoSuchElementException()
            }.map { list ->
                val stations = list.data ?: emptyList()
                stations.map { station ->
                    val routes = station.routes.map {
                        UiRoute(it, it, Color.Black)
                    }
                    UiStation(station.id, station.name, station.latitude, station.longitude, routes)
                }
            }.onSuccess { stations ->
                Timber.d("Stations success, size = ${stations.size}")
                _uiStateFlow.value = StationsState(stations = stations.sortedBy { it.name }, loading = false)
            }.onFailure { err ->
                Timber.e(err)
                _uiStateFlow.update { it.copy(loading = false) }
            }
        }
    }
}

data class StationsState(
    val stations: List<UiStation>,
    val loading: Boolean,
)
