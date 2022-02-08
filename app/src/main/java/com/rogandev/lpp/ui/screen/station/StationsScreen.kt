package com.rogandev.lpp.ui.screen.station

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rogandev.lpp.ui.component.BackTopAppBar

@Composable
fun StationScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            BackTopAppBar(title = "Stop", onBackClick = onBackClick)
        },

        content = { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            }
        },
    )
}
