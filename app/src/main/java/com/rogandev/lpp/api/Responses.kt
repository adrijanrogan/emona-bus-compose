package com.rogandev.lpp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApiList<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: List<T>?,
)

// Route

@Serializable
class ApiRoute(
    @SerialName("trip_id")
    val tripId: String,
    @SerialName("route_id")
    val routeId: String,
    @SerialName("route_number")
    val routeNumber: String,
    @SerialName("route_name")
    val routeName: String,
)

// Station

@Serializable
class ApiStationDetails(
    @SerialName("ref_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("route_groups_on_station")
    val routeGroups: List<String>
)
