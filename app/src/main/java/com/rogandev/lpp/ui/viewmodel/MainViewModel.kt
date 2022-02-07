package com.rogandev.lpp.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.StationRepository
import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(MainScreenState(emptyList(), emptyList(), emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }
            delay(1000)

            stationRepository.getStations().map { list ->
                list.map {
                    val routes = it.routes.map { r ->
                        val color = routeColor(r)
                        UiRoute(r, r, Color(color))
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

    private fun routeColor(route: String): Int {
        if (route.startsWith("N")) {
            return routeColor(route.substring(1))
        }

        return when (route) {
            "01", "1" -> android.graphics.Color.parseColor("#ca3736")
            "01B", "1B" -> android.graphics.Color.parseColor("#ca3736")
            "01D", "1D" -> android.graphics.Color.parseColor("#ca3736")
            "02", "2" -> android.graphics.Color.parseColor("#8c8741")
            "03", "3" -> android.graphics.Color.parseColor("#ec5a3a")
            "03B", "3B" -> android.graphics.Color.parseColor("#ec5a3a")
            "03G", "3G" -> android.graphics.Color.parseColor("#ec5a3a")
            "05", "5" -> android.graphics.Color.parseColor("#9f549d")
            "06", "6" -> android.graphics.Color.parseColor("#939598")
            "06B", "6B" -> android.graphics.Color.parseColor("#b0b0b1")
            "07", "7" -> android.graphics.Color.parseColor("#0eb9db")
            "07L", "7L" -> android.graphics.Color.parseColor("#0eb9db")
            "08", "8" -> android.graphics.Color.parseColor("#036aaf")
            "09", "9" -> android.graphics.Color.parseColor("#88aacd")
            "11" -> android.graphics.Color.parseColor("#ecc23b")
            "11B" -> android.graphics.Color.parseColor("#ecc23b")
            "12" -> android.graphics.Color.parseColor("#284ba0")
            "12D" -> android.graphics.Color.parseColor("#849daa")
            "13" -> android.graphics.Color.parseColor("#d0d34d")
            "14" -> android.graphics.Color.parseColor("#ef5ba1")
            "15" -> android.graphics.Color.parseColor("#a3238e")
            "18" -> android.graphics.Color.parseColor("#885835")
            "18L" -> android.graphics.Color.parseColor("#885835")
            "19B" -> android.graphics.Color.parseColor("#e89db4")
            "19I" -> android.graphics.Color.parseColor("#e89db4")
            else -> android.graphics.Color.parseColor("#b0b0b0")
        }
    }
}

data class MainScreenState(
    val nearbyStations: List<UiStation>,
    val stations: List<UiStation>,
    val routes: List<UiRoute>,
    val loading: Boolean,
)
