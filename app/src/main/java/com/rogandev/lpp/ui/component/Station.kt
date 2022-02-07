package com.rogandev.lpp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.ui.model.UiStation

@Composable
fun StationList(
    modifier: Modifier = Modifier,
    stations: List<UiStation>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 10.dp),
    ) {
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

            // Add the station layout
            StationRow(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                station = item
            )
        }
    }
}

@Composable
fun StationRow(
    modifier: Modifier = Modifier,
    station: UiStation,
    onStationClick: (UiStation) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = { onStationClick(station) }
            )
            .then(modifier),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(end = 10.dp)) {
            Text(
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                text = station.name,
            )
            Text(
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                text = station.id,
            )
        }

        RouteIndicators(modifier = Modifier
            .weight(1f)
            .padding(start = 10.dp), routes = station.routes)
    }
}
