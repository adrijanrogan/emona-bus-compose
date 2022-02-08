package com.rogandev.lpp.ui.model

data class UiRoute(
    val tripId: String,
    val routeId: String,
    val routeGroup: UiRouteGroup,
    val routeStart: String,
    val routeMid: List<String>,
    val routeEnd: String
)
