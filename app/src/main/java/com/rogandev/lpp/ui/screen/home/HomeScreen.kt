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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rogandev.lpp.R
import com.rogandev.lpp.ui.component.StationCard
import com.rogandev.lpp.ui.model.UiStation
import com.rogandev.lpp.ui.screen.Navigation

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val state by viewModel.uiStateFlow.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                color = MaterialTheme.colors.primary,
                elevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        },

        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) {

                HomeContent(state = state, navController = navController)

                if (state.loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        },
    )
}

@Composable
fun HomeContent(state: HomeScreenState, navController: NavController) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            SquareCard(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.stations_title),
                iconResId = R.drawable.ic_bus,
                onActionClick = {
                    navController.navigate(Navigation.Stations.route)
                })
            SquareCard(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.routes_title),
                iconResId = R.drawable.ic_trip,
                onActionClick = {
                    navController.navigate(Navigation.Routes.route)
                })
        }

        if (state.nearbyStations.isNotEmpty()) {
            Divider()
            Spacer(modifier = Modifier.height(10.dp))
            StationsNearby(stations = state.nearbyStations, onStationClick = {
                navController.navigate(Navigation.Station.build(it.id))
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun StationsNearby(modifier: Modifier = Modifier, stations: List<UiStation>, onStationClick: (UiStation) -> Unit) {
    Column(modifier = modifier) {
        Section(
            modifier = Modifier.fillMaxWidth(),
            nameText = stringResource(id = R.string.home_stations_nearby),
            actionText = stringResource(id = R.string.general_more)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(stations.chunked(2)) { columnStations ->
                Column {
                    columnStations.forEachIndexed { index, station ->
                        StationCard(
                            station = station,
                            modifier = Modifier.width(220.dp),
                            onStationClick = onStationClick
                        )
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
            fontSize = 22.sp,
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
