package com.example.travelmemories.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travelmemories.data.local.entity.MediaReferenceEntity

@Dao
interface MediaReferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reference: MediaReferenceEntity): Long

    @Query("SELECT * FROM media_references ORDER BY id DESC LIMIT 1")
    suspend fun getLatest(): MediaReferenceEntity?
}
