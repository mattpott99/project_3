package com.example.travelmemories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.travelmemories.ui.navigation.TravelMemoriesNavHost
import com.example.travelmemories.ui.state.AppStartupState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as TravelMemoriesApp
        val startupState = if (app.appContainer != null) {
            AppStartupState.Ready(app.appContainer!!)
        } else {
            AppStartupState.Failed(app.initError ?: "Unknown error")
        }

        setContent {
            MaterialTheme {
                Surface {
                    TravelMemoriesAppRoot(startupState)
                }
            }
        }
    }
}

@Composable
private fun TravelMemoriesAppRoot(startupState: AppStartupState) {
    val state = remember(startupState) { startupState }
    TravelMemoriesNavHost(state)
}
