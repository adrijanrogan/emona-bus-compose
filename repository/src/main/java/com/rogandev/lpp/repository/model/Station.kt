package com.rogandev.lpp.repository.model

data class Station(
    val code: String,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val routeGroups: List<String>,
)
