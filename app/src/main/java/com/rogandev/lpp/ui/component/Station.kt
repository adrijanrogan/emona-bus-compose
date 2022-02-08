package com.rogandev.lpp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.ui.model.UiStation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StationCard(modifier: Modifier = Modifier, station: UiStation, onStationClick: (UiStation) -> Unit) {
    // Elevated surface for station content
    Surface(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
        content = {
            StationContent(
                modifier = Modifier.padding(10.dp),
                station = station
            )
        },
        onClick = {
            onStationClick(station)
        }
    )
}

@Composable
fun StationContent(modifier: Modifier = Modifier, station: UiStation) {
    // Show routes on the station, the station name and the station ID
    Column(modifier = modifier) {

        // Routes on the station
        RouteIndicators(routeGroups = station.routeGroups, size = 30.dp, spacing = 5.dp)

        Spacer(modifier = Modifier.height(10.dp))

        // Station name
        Text(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            text = station.name
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Station ID
        Text(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            text = station.id
        )
    }
}
