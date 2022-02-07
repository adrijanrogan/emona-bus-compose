package com.rogandev.lpp.repository

interface StationRepository {

    suspend fun getStations(): Result<List<Station>>
}
