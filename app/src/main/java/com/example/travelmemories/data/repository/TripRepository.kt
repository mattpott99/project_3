package com.example.travelmemories.data.repository

import com.example.travelmemories.data.local.dao.TripDao
import com.example.travelmemories.data.local.entity.TripEntity
import com.example.travelmemories.domain.Trip
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepository(private val tripDao: TripDao) {
    fun observeTrips(): Flow<List<Trip>> = tripDao.observeTrips().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun hasTripNamed(name: String): Boolean = tripDao.hasTripNamed(name)

    suspend fun createTrip(
        name: String,
        startDate: LocalDate,
        endDate: LocalDate?,
        coverImageUrl: String?
    ): Long {
        val now = Instant.now()
        return tripDao.insertTrip(
            TripEntity(
                name = name,
                startDateEpochDay = startDate.toEpochDay(),
                endDateEpochDay = endDate?.toEpochDay(),
                coverImageUrl = coverImageUrl,
                archived = false,
                createdAtEpochMillis = now.toEpochMilli()
            )
        )
    }

    suspend fun archiveTrip(tripId: Long) {
        tripDao.archiveTrip(tripId)
    }

    suspend fun restoreTrip(tripId: Long) {
        tripDao.restoreTrip(tripId)
    }
}

private fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    name = name,
    startDate = LocalDate.ofEpochDay(startDateEpochDay),
    endDate = endDateEpochDay?.let { LocalDate.ofEpochDay(it) },
    coverImageUrl = coverImageUrl,
    archived = archived,
    createdAt = Instant.ofEpochMilli(createdAtEpochMillis)
)
