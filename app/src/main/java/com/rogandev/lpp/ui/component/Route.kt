package com.rogandev.lpp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
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
import com.rogandev.lpp.ui.model.UiRouteGroup

@Composable
fun RouteIndicator(size: Dp, routeGroup: UiRouteGroup) {
    Box(
        modifier = Modifier
            .requiredSize(size)
            .background(routeGroup.color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = routeGroup.name,
        )
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
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.End),
        modifier = modifier
    ) {
        routeGroups.forEach { route ->
            RouteIndicator(routeGroup = route, size = size)
        }
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
