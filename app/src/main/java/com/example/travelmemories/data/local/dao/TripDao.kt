package com.example.travelmemories.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travelmemories.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY createdAtEpochMillis DESC")
    fun observeTrips(): Flow<List<TripEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM trips WHERE name = :name)")
    suspend fun hasTripNamed(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrip(trip: TripEntity): Long

    @Query("UPDATE trips SET archived = 1 WHERE id = :tripId")
    suspend fun archiveTrip(tripId: Long)

    @Query("UPDATE trips SET archived = 0 WHERE id = :tripId")
    suspend fun restoreTrip(tripId: Long)
}
