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
import com.rogandev.lpp.ui.model.UiRoute

@Composable
fun RouteIndicator(size: Dp, route: UiRoute) {
    Box(
        modifier = Modifier
            .requiredSize(size)
            .background(route.color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = route.name,
        )
    }
}

@Composable
fun RouteIndicators(
    modifier: Modifier = Modifier,
    spacing: Dp = 5.dp,
    size: Dp = 30.dp,
    routes: List<UiRoute>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.End),
        modifier = modifier
    ) {
        routes.forEach { route ->
            RouteIndicator(route = route, size = size)
        }
    }
}

@Composable
@Preview
fun RouteIndicatorsPreview() {
    val routes = listOf(
        UiRoute("1", "1", Color(0xffca3736)),
        UiRoute("2", "2", Color(0xff8c8741)),
        UiRoute("3", "3", Color(0xffec5a3a)),
        UiRoute("5", "5", Color(0xff9f549d)),
        UiRoute("6B", "6B", Color(0xffb0b0b1)),
        UiRoute("14", "14", Color(0xffef5ba1)),
        UiRoute("19B", "19B", Color(0xffe89db4)),
    )

    RouteIndicators(routes = routes)
}
