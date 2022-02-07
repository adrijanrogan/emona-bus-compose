package com.rogandev.lpp.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rogandev.lpp.ui.component.RouteIndicator
import com.rogandev.lpp.ui.component.RouteIndicators
import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiStation
import com.rogandev.lpp.ui.theme.EmonaTheme
import com.rogandev.lpp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                EmonaTheme {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {

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
            LazyColumn {

                if (state.loading) {
                    item {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }

                if (state.nearbyStations.isNotEmpty()) {
                    item {
                        StationsNearby(stations = state.nearbyStations)
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                    }
                }

                if (state.stations.isNotEmpty()) {
                    item {
                        StationsAll(highlighted = state.stations)
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                    }
                }

                if (state.routes.isNotEmpty()) {
                    item {
                        Section(modifier = Modifier.fillMaxWidth(), nameText = "Routes", actionText = "Show all")
                    }

                    items(state.routes) { route ->

                        // Elevated surface for station
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            RouteContent(route = UiRoute("1", "1", Color.Black))
                        }

                        Spacer(modifier = Modifier.height(15.dp))
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
            items(stations) { station ->
                StationCard(station = station, modifier = Modifier.width(200.dp))
            }
        }
    }
}

@Composable
fun StationsAll(modifier: Modifier = Modifier, highlighted: List<UiStation>) {
    Column(modifier = modifier) {
        Section(modifier = Modifier.fillMaxWidth(), nameText = "Stops", actionText = "Show all")
        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            items(highlighted) { station ->
                StationCard(station = station, modifier = Modifier.width(200.dp))
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
        RouteIndicators(routes = station.routes, size = 25.dp, spacing = 3.dp)

        Spacer(modifier = Modifier.height(10.dp))

        // Station name
        Text(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            text = station.name
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Station ID
        Text(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            text = station.id
        )
    }
}

@Composable
fun RouteContent(route: UiRoute) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(10.dp)) {
            RouteIndicator(
                size = 35.dp,
                route = route,
            )
        }

        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp)) {

            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                text = "Start name"
            )

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                text = "Middle name"
            )

            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                text = "End name"
            )
        }
    }
}
