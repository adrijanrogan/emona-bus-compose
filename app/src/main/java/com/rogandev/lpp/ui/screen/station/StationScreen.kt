package com.rogandev.lpp.ui.screen.station

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rogandev.lpp.ui.component.BackTopAppBar

@Composable
fun StationScreen(state: StationScreenState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = "Postajališče", onBackClick = onBackClick)
        },

        content = { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                StationDetails(state)

                if (state.loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
    )
}

@Composable
fun StationDetails(state: StationScreenState) {
    LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {

        if (state.messages.isNotEmpty()) {
            items(items = state.messages) { item ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    text = item,
                )
            }
        }
    }
}
