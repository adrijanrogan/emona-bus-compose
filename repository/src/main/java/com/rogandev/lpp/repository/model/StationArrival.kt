package com.rogandev.lpp.repository.model

data class StationArrival(
    val routeGroup: String,
    val tripName: String,
    val eta: Int,
)
