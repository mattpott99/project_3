package com.example.travelmemories.data.storage

import android.content.Context
import java.io.File
import java.time.Instant

class FileStorage(private val context: Context) {
    fun createSmokeTestFile(): File {
        val file = File(context.filesDir, "smoke_test_${Instant.now().toEpochMilli()}.txt")
        file.writeText("Travel Memories smoke test file.")
        return file
    }
}
