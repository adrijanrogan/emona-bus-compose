package com.rogandev.lpp.repository.cache.meta

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Metadata @Inject constructor(@ApplicationContext context: Context) {

    private val sp = context.getSharedPreferences("metadata", Context.MODE_PRIVATE)

    fun getStationRefreshTime(): Instant {
        val epochSecond = sp.getLong("refresh_stations", 0)
        return Instant.ofEpochSecond(epochSecond)
    }

    fun putStationRefreshTime(time: Instant) {
        sp.edit {
            putLong("refresh_stations", time.epochSecond)
        }
    }
}
