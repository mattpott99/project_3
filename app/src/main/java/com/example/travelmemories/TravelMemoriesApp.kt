package com.example.travelmemories

import android.app.Application
import com.example.travelmemories.di.AppContainer

class TravelMemoriesApp : Application() {
    var appContainer: AppContainer? = null
        private set
    var initError: String? = null
        private set

    override fun onCreate() {
        super.onCreate()
        try {
            appContainer = AppContainer(this)
        } catch (exception: Exception) {
            initError = exception.localizedMessage ?: "Unknown initialization error"
        }
    }
}
