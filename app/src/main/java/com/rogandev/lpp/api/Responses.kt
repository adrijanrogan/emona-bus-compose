package com.rogandev.lpp.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LppList<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: List<T>?,
)

@Serializable
class StationDetails(
    @SerialName("ref_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("route_groups_on_station")
    val routes: List<String>
)
