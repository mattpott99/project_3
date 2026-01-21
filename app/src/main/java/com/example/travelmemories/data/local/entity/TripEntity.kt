package com.example.travelmemories.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trips",
    indices = [Index(value = ["name"], unique = true)]
)
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long?,
    val coverImageUrl: String?,
    val archived: Boolean,
    val createdAtEpochMillis: Long
)
