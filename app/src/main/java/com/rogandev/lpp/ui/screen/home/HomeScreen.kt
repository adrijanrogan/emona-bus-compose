package com.rogandev.lpp.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rogandev.lpp.ui.component.RouteIndicators
import com.rogandev.lpp.ui.model.UiStation
import com.rogandev.lpp.ui.screen.Destination

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val state by viewModel.uiStateFlow.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                color = MaterialTheme.colors.background,
                elevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Emona Bus", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        },

        content = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                if (state.loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                if (state.nearbyStations.isNotEmpty()) {
                    StationsNearby(stations = state.nearbyStations)
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                }

                if (state.cards.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    val screenWidth = LocalConfiguration.current.screenWidthDp
                    val itemsPerRow = (screenWidth / 180)

                    state.cards.chunked(itemsPerRow).forEach { cardChunk ->
                        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {

                            cardChunk.forEach { card ->
                                SquareCard(modifier = Modifier.weight(1f), text = card.title, iconResId = card.iconResId, onActionClick = {
                                    navController.navigate(Destination.Stations.route)
                                })
                            }

                            repeat(itemsPerRow - cardChunk.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun StationsNearby(modifier: Modifier = Modifier, stations: List<UiStation>) {
    Column(modifier = modifier) {
        Section(modifier = Modifier.fillMaxWidth(), nameText = "Nearby stops", actionText = "Show more")
        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            items(stations.chunked(2)) { columnStations ->
                Column {
                    columnStations.forEachIndexed { index, station ->
                        StationCard(station = station, modifier = Modifier.width(220.dp))
                        if (index != columnStations.lastIndex) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Section(modifier: Modifier = Modifier, nameText: String, actionText: String, onActionClick: () -> Unit = {}) {
    // Section title at the start and an action at the end
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Section title on the left
        Text(
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            text = nameText,
        )

        // Show more action on the right
        Text(
            modifier = Modifier
                .padding(10.dp)
                .clickable { onActionClick() }
                .padding(10.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = actionText,
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.primary,
        )
    }
}

@Composable
fun StationCard(modifier: Modifier = Modifier, station: UiStation) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SquareCard(modifier: Modifier = Modifier, text: String, @DrawableRes iconResId: Int, onActionClick: () -> Unit) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        elevation = 2.dp,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colors.surface,
        onClick = {
            onActionClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = ImageVector.vectorResource(id = iconResId),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.padding(10.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                text = text,
            )
        }
    }
}
