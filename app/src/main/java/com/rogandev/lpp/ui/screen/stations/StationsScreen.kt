package com.rogandev.lpp.ui.screen.stations

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rogandev.lpp.ui.component.BackTopAppBar
import com.rogandev.lpp.ui.screen.home.StationCard

@Composable
fun StationsScreen(state: StationsScreenState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = "Stops", onBackClick = onBackClick)
        },

        content = {
            LazyColumn {

                if (state.loading) {
                    item {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                itemsIndexed(items = state.stations, key = { _, item -> item.id }) { idx, item ->

                    // If the previous station starts with a different letter, add a divider
                    val previous = state.stations.getOrNull(idx - 1)
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
                        onStationClick = {}
                    )
                }
            }
        },
    )
}
