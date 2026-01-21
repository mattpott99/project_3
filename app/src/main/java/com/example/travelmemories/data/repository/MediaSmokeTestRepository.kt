package com.example.travelmemories.data.repository

import com.example.travelmemories.data.local.dao.MediaReferenceDao
import com.example.travelmemories.data.local.entity.MediaReferenceEntity
import com.example.travelmemories.data.storage.FileStorage
import java.time.Instant

class MediaSmokeTestRepository(
    private val fileStorage: FileStorage,
    private val mediaReferenceDao: MediaReferenceDao
) {
    suspend fun runSmokeTest(): MediaSmokeTestResult {
        val existing = mediaReferenceDao.getLatest()
        val validated = existing?.takeIf { reference ->
            reference.uri.isNotBlank() && fileStorageExists(reference.uri)
        }

        if (validated != null) {
            return MediaSmokeTestResult.Success(validated.uri)
        }

        return try {
            val file = fileStorage.createSmokeTestFile()
            val reference = MediaReferenceEntity(
                uri = file.absolutePath,
                createdAt = Instant.now().toString()
            )
            mediaReferenceDao.insert(reference)
            val reloaded = mediaReferenceDao.getLatest()
            if (reloaded != null && fileStorageExists(reloaded.uri)) {
                MediaSmokeTestResult.Success(reloaded.uri)
            } else {
                MediaSmokeTestResult.Failure("Failed to reload stored file reference.")
            }
        } catch (exception: Exception) {
            MediaSmokeTestResult.Failure(exception.localizedMessage ?: "Unknown storage error")
        }
    }

    private fun fileStorageExists(path: String): Boolean {
        return runCatching { java.io.File(path).exists() }.getOrDefault(false)
    }
}

sealed class MediaSmokeTestResult {
    data class Success(val uri: String) : MediaSmokeTestResult()
    data class Failure(val reason: String) : MediaSmokeTestResult()
}
