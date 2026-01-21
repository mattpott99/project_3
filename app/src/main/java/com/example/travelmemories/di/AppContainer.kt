package com.example.travelmemories.di

import android.content.Context
import androidx.room.Room
import com.example.travelmemories.data.local.AppDatabase
import com.example.travelmemories.data.repository.MediaSmokeTestRepository
import com.example.travelmemories.data.repository.TripRepository
import com.example.travelmemories.data.storage.FileStorage

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "travel_memories.db"
    )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()

    private val fileStorage = FileStorage(appContext)

    val mediaSmokeTestRepository = MediaSmokeTestRepository(
        fileStorage = fileStorage,
        mediaReferenceDao = database.mediaReferenceDao()
    )

    val tripRepository = TripRepository(database.tripDao())
}
