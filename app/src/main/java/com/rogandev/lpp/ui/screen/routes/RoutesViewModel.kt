package com.rogandev.lpp.ui.screen.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.ktx.mapIterable
import com.rogandev.lpp.repository.BusRepository
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
    private val busRepository: BusRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(RoutesScreenState(emptyList(), false))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {

            _uiStateFlow.update { it.copy(loading = true) }

            busRepository.getActiveRoutes().mapIterable { apiRoute ->
                val routeNameParts = apiRoute.routeName.split('-').map { it.trim() }
                val (routeStart, routeMid, routeEnd) = when (routeNameParts.size) {
                    1 -> Triple("", emptyList(), routeNameParts.first())
                    2 -> Triple(routeNameParts[0], emptyList(), routeNameParts[1])
                    else -> Triple(routeNameParts.first(), routeNameParts.subList(1, routeNameParts.lastIndex), routeNameParts.last())
                }

                val routeGroup = UiRouteGroup.fromName(apiRoute.routeNumber)

                UiRoute(
                    tripId = apiRoute.tripId,
                    routeId = apiRoute.routeId,
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
