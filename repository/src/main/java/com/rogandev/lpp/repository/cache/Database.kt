package com.rogandev.lpp.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rogandev.lpp.repository.cache.dao.StationDao

@Database(
    entities = [DbStation::class],
    version = 1,
    exportSchema = false,
)
abstract class Database : RoomDatabase() {

    abstract fun stationDao(): StationDao
}
