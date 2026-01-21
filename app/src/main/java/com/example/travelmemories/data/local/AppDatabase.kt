package com.example.travelmemories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travelmemories.data.local.dao.MediaReferenceDao
import com.example.travelmemories.data.local.entity.MediaReferenceEntity
import com.example.travelmemories.data.local.entity.TripEntity

@Database(
    entities = [TripEntity::class, MediaReferenceEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaReferenceDao(): MediaReferenceDao
}
