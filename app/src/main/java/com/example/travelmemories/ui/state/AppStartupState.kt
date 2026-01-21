package com.example.travelmemories.ui.state

import com.example.travelmemories.di.AppContainer

sealed class AppStartupState {
    data class Ready(val container: AppContainer) : AppStartupState()
    data class Failed(val message: String) : AppStartupState()
}
