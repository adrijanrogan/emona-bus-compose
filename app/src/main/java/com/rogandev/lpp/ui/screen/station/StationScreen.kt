package com.rogandev.lpp.ui.screen.station

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.rogandev.lpp.ui.component.ArrivalCard
import com.rogandev.lpp.ui.component.BackTopAppBar

@Composable
fun StationScreen(state: StationScreenState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = "Postajališče", onBackClick = onBackClick)
        },

        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                StationDetails(state)

                if (state.loadingMessages || state.loadingArrivals) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
    )
}

@Composable
fun StationDetails(state: StationScreenState) {
    LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {

        // Messages

        item {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                text = "Obvestila",
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (state.messages.isNotEmpty()) {
            items(items = state.messages) { item ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    text = item,
                )
            }
        } else {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .placeholder(state.loadingMessages, highlight = PlaceholderHighlight.fade()),
                    text = "Na izbranem postajališču trenutno ni nobenih obvestil.",
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Divider()
        }

        // Arrivals

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                text = "Prihodi",
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (state.arrivalGroups.isNotEmpty()) {
            items(items = state.arrivalGroups) { item ->
                ArrivalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    arrivalGroup = item,
                    onArrivalClick = {},
                )
            }
        } else {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .placeholder(state.loadingArrivals, highlight = PlaceholderHighlight.fade()),
                    text = "Za izbrano postajališče trenutno ni napovedanih prihodov.",
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}
