package com.rogandev.lpp.ui.screen.stations

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
import com.rogandev.lpp.ui.component.StationCard
import com.rogandev.lpp.ui.model.UiStation

@Composable
fun StationsScreen(state: StationsScreenState, onBackClick: () -> Unit, onStationClick: (UiStation) -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = stringResource(id = R.string.stations_title), onBackClick = onBackClick)
        },

        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                StationList(stations = state.stations, onStationClick = onStationClick)

                if (state.loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
    )
}

@Composable
fun StationList(stations: List<UiStation>, onStationClick: (UiStation) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {
        itemsIndexed(items = stations, key = { _, item -> item.id }) { idx, item ->

            // If the previous station starts with a different letter, add a divider
            val previous = stations.getOrNull(idx - 1)
            val previousChar = previous?.name?.firstOrNull()
            val currentChar = item.name.firstOrNull()
            if (previousChar != null && currentChar != null && previousChar != currentChar) {
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
            }

            StationCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                station = item,
                onStationClick = {
                    onStationClick(item)
                }
            )
        }
    }
}
