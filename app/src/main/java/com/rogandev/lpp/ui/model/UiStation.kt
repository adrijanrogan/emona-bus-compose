package com.rogandev.lpp.ui.model

data class UiStation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val routeGroups: List<UiRouteGroup>
)
