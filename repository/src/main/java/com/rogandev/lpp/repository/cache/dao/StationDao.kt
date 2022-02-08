package com.rogandev.lpp.repository.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rogandev.lpp.repository.cache.DbStation
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {

    @Query("SELECT * FROM DbStation")
    fun getAll(): Flow<List<DbStation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stations: List<DbStation>)
}
