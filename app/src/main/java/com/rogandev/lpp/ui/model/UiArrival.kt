package com.rogandev.lpp.ui.model

data class UiArrival(
    val routeGroup: UiRouteGroup,
    val destination: String,
    val etas: List<String>,
)
