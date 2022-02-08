package com.rogandev.lpp.ui.screen.stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.BusRepository
import com.rogandev.lpp.ui.model.UiRouteGroup
import com.rogandev.lpp.ui.model.UiStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val busRepository: BusRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(StationsScreenState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }
            delay(1000)

            busRepository.getStations().map { list ->
                list.map {
                    val routes = it.routeGroups.map { routeGroup ->
                        UiRouteGroup.fromName(routeGroup)
                    }
                    UiStation(it.id, it.name, it.latitude, it.longitude, routes)
                }.sortedBy {
                    it.name
                }
            }.onSuccess { stations ->
                _uiStateFlow.update {
                    it.copy(stations = stations, loading = false)
                }
            }.onFailure {
                _uiStateFlow.update { it.copy(loading = false) }
            }
        }
    }
}

data class StationsScreenState(
    val stations: List<UiStation>,
    val loading: Boolean,
)
