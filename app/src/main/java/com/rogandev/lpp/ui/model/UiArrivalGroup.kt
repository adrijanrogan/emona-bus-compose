package com.rogandev.lpp.ui.model

data class UiArrivalGroup(
    val routeGroup: UiRouteGroup,
    val destination: String,
    val arrivals: List<UiArrival>,
)

data class UiArrival(
    val delta: String,
    val garage: Boolean,
)
