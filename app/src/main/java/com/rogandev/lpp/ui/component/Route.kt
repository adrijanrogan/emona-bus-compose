package com.rogandev.lpp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiRouteGroup

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RouteCard(modifier: Modifier = Modifier, route: UiRoute, onRouteClick: (UiRoute) -> Unit) {
    // Elevated surface for station content
    Surface(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
        content = {
            RouteContent(
                modifier = Modifier.padding(10.dp),
                route = route
            )
        },
        onClick = {
            onRouteClick(route)
        }
    )
}

@Composable
fun RouteContent(modifier: Modifier = Modifier, route: UiRoute) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        // Route indicator on the left
        RouteIndicator(size = 50.dp, routeGroup = route.routeGroup)

        Spacer(modifier = Modifier.width(20.dp))

        // Route directions
        Column {
            Text(text = route.routeEnd, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            route.routeMid.forEach {
                Text(text = "• $it", fontSize = 16.sp)
            }
            Text(text = "• ${route.routeStart}", fontSize = 16.sp)
        }
    }
}

@Composable
fun RouteIndicators(
    modifier: Modifier = Modifier,
    spacing: Dp = 5.dp,
    size: Dp = 30.dp,
    routeGroups: List<UiRouteGroup>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.Start),
        modifier = modifier
    ) {
        routeGroups.forEach { route ->
            RouteIndicator(routeGroup = route, size = size)
        }
    }
}

@Composable
fun RouteIndicator(size: Dp, routeGroup: UiRouteGroup) {
    Box(
        modifier = Modifier
            .requiredSize(size)
            .background(routeGroup.color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontSize = (size.value / 2.5).sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = routeGroup.name,
        )
    }
}

@Composable
@Preview
fun RouteCardPreview() {
    val routes = listOf(
        UiRoute(
            tripId = "",
            routeId = "",
            routeGroup = UiRouteGroup.fromName("7"),
            routeMid = emptyList(),
            routeStart = "Nove Jarše",
            routeEnd = "Pržan"
        ),
        UiRoute(
            tripId = "",
            routeId = "",
            routeGroup = UiRouteGroup.fromName("7L"),
            routeStart = "Letališka",
            routeMid = listOf("Nove Jarše"),
            routeEnd = "Pržan"
        )
    )

    Column(modifier = Modifier.width(400.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        routes.forEach { RouteCard(
            modifier = Modifier.fillMaxWidth(),
            route = it, onRouteClick = {}
        ) }
    }
}

@Composable
@Preview
fun RouteIndicatorsPreview() {
    val routes = listOf(
        UiRouteGroup.fromName("1"),
        UiRouteGroup.fromName("2"),
        UiRouteGroup.fromName("3"),
        UiRouteGroup.fromName("5"),
        UiRouteGroup.fromName("6"),
        UiRouteGroup.fromName("6B"),
        UiRouteGroup.fromName("14"),
        UiRouteGroup.fromName("18"),
        UiRouteGroup.fromName("19B"),
    )

    RouteIndicators(routeGroups = routes)
}
