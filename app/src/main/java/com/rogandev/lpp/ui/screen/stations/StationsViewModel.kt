package com.rogandev.lpp.ui.screen.stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.Repository
import com.rogandev.lpp.ui.model.UiRouteGroup
import com.rogandev.lpp.ui.model.UiStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(StationsScreenState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }

            repository.getStations().map { list ->
                list.map {
                    val routes = it.routeGroups.map { routeGroup ->
                        UiRouteGroup.fromName(routeGroup)
                    }
                    UiStation(it.code, it.name, it.latitude, it.longitude, routes)
                }.sortedBy {
                    it.name
                }
            }.onEach { stations ->
                _uiStateFlow.update {
                    it.copy(stations = stations, loading = false)
                }
            }.launchIn(viewModelScope)
        }
    }
}

data class StationsScreenState(
    val stations: List<UiStation>,
    val loading: Boolean,
)
