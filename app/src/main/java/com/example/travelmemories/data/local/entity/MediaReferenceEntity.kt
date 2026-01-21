package com.example.travelmemories.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_references")
data class MediaReferenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val createdAt: String
)
