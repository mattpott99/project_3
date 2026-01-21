package com.example.travelmemories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.travelmemories.data.local.dao.MediaReferenceDao
import com.example.travelmemories.data.local.dao.TripDao
import com.example.travelmemories.data.local.entity.MediaReferenceEntity
import com.example.travelmemories.data.local.entity.TripEntity

@Database(
    entities = [TripEntity::class, MediaReferenceEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaReferenceDao(): MediaReferenceDao
    abstract fun tripDao(): TripDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE trips ADD COLUMN startDateEpochDay INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE trips ADD COLUMN endDateEpochDay INTEGER"
                )
                database.execSQL(
                    "ALTER TABLE trips ADD COLUMN coverImageUrl TEXT"
                )
                database.execSQL(
                    "ALTER TABLE trips ADD COLUMN archived INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE trips ADD COLUMN createdAtEpochMillis INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
