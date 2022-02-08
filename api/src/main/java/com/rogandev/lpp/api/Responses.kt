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

@Serializable
class ApiObject<T>(
    @SerialName("data")
    val data: T?,
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
class ApiStationArrivals(
    @SerialName("arrivals")
    val arrivals: List<ApiStationArrival>
)

@Serializable
class ApiStationArrival(
    @SerialName("eta_min")
    val eta: Int,
    @SerialName("route_name")
    val routeName: String,
    @SerialName("trip_name")
    val tripName: String,
)

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
