package com.rogandev.lpp.ui.screen.home

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
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(HomeScreenState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }

            repository.getStations().map { list ->
                list.map {
                    val routeGroups = it.routeGroups.map { routeGroup ->
                        UiRouteGroup.fromName(routeGroup)
                    }
                    UiStation(it.code, it.name, it.latitude, it.longitude, routeGroups)
                }.sortedBy {
                    it.name
                }
            }.onEach { stations ->
                _uiStateFlow.update {
                    it.copy(nearbyStations = stations.shuffled().take(10), loading = false)
                }
            }.launchIn(viewModelScope)
        }
    }
}

data class HomeScreenState(
    val nearbyStations: List<UiStation>,
    val loading: Boolean,
)
