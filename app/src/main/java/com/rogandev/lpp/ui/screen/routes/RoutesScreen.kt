package com.rogandev.lpp.ui.screen.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogandev.lpp.R
import com.rogandev.lpp.ui.component.BackTopAppBar
import com.rogandev.lpp.ui.component.RouteCard
import com.rogandev.lpp.ui.model.UiRoute

@Composable
fun RoutesScreen(state: RoutesScreenState, onBackClick: () -> Unit, onRouteClick: (UiRoute) -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = stringResource(id = R.string.routes_title), onBackClick = onBackClick)
        },

        content = { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                RouteList(stations = state.routes, onRouteClick = onRouteClick)

                if (state.loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
    )
}

@Composable
fun RouteList(stations: List<UiRoute>, onRouteClick: (UiRoute) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {
        itemsIndexed(items = stations, key = { _, item -> item.tripId }) { idx, item ->

            // If the previous route starts with a different number, add a divider
            val previous = stations.getOrNull(idx - 1)
            val previousNumber = previous?.routeGroup?.name?.filter { it.isDigit() }?.toIntOrNull() ?: 0
            val currentNumber = item.routeGroup.name.filter { it.isDigit() }.toIntOrNull() ?: 0

            if (previousNumber != 0 && previousNumber != currentNumber) {
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
            }

            RouteCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                route = item,
                onRouteClick = {
                    onRouteClick(item)
                }
            )
        }
    }
}
