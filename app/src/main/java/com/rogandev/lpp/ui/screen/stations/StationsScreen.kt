package com.rogandev.lpp.ui.screen.stations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.ui.screen.home.StationCard

@Composable
fun StationsScreen(state: StationsScreenState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                color = MaterialTheme.colors.background,
                elevation = 4.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onBackClick() }
                            .padding(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Stops", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
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
                        station = item
                    )
                }
            }
        },
    )
}
