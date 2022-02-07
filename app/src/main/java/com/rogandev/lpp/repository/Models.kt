package com.rogandev.lpp.repository

data class Station(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val routeGroups: List<String>
)
