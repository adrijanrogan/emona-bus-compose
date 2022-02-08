package com.rogandev.lpp.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbStation(
    @PrimaryKey
    val code: String,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val routeGroups: String,
)
