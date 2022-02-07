package com.rogandev.lpp.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.R
import com.rogandev.lpp.repository.StationRepository
import com.rogandev.lpp.ui.model.UiRouteGroup
import com.rogandev.lpp.ui.model.UiStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val cards = listOf(
        MainScreenCard("Stops", R.drawable.ic_bus),
        MainScreenCard("Routes", R.drawable.ic_trip),
        MainScreenCard("Help", R.drawable.ic_book),
    )

    private val _uiStateFlow = MutableStateFlow(MainScreenState(emptyList(), cards,false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }
            delay(1000)

            stationRepository.getStations().map { list ->
                list.map {
                    val routeGroups = it.routeGroups.map { routeGroup ->
                        UiRouteGroup.fromName(routeGroup)
                    }
                    UiStation(it.id, it.name, it.latitude, it.longitude, routeGroups)
                }.sortedBy {
                    it.name
                }
            }.onSuccess { stations ->
                _uiStateFlow.update {
                    it.copy(nearbyStations = stations.shuffled().take(10), loading = false)
                }
            }.onFailure {
                _uiStateFlow.update { it.copy(loading = false) }
            }
        }
    }

    fun onStationsClick() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.NavigateStations)
        }
    }
}

data class MainScreenState(
    val nearbyStations: List<UiStation>,
    val cards: List<MainScreenCard>,
    val loading: Boolean,
)

data class MainScreenCard(
    val title: String,
    @DrawableRes val iconResId: Int,
)

sealed class HomeEvent {
    object NavigateStations : HomeEvent()
}
