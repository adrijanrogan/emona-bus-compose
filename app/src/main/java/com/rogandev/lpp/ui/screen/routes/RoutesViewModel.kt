package com.rogandev.lpp.ui.screen.routes

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.ktx.mapList
import com.rogandev.lpp.repository.Repository
import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiRouteGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(RoutesScreenState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }

            repository.getActiveRoutes().mapList { route ->
                val tripNameParts = route.tripName.split('-').map { it.trim().lowercase().capitalize(Locale("sl")) }
                val (routeStart, routeMid, routeEnd) = when (tripNameParts.size) {
                    1 -> Triple("", emptyList(), tripNameParts.first())
                    2 -> Triple(tripNameParts[0], emptyList(), tripNameParts[1])
                    else -> Triple(tripNameParts.first(), tripNameParts.subList(1, tripNameParts.lastIndex), tripNameParts.last())
                }

                val routeGroup = UiRouteGroup.fromName(route.routeGroup)

                UiRoute(
                    tripId = route.tripId,
                    routeId = route.routeId,
                    routeGroup = routeGroup,
                    routeStart = routeStart,
                    routeMid = routeMid,
                    routeEnd = routeEnd
                )
            }.map { routes ->
                routes.sortedWith(RouteComparator)
            }.onSuccess { routes ->
                _uiStateFlow.update {
                    it.copy(routes = routes, loading = false)
                }
            }.onFailure { err ->
                Timber.e(err)
                _uiStateFlow.update { it.copy(loading = false) }
            }
        }
    }
}

data class RoutesScreenState(
    val routes: List<UiRoute>,
    val loading: Boolean,
)
