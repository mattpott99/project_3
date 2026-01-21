package com.example.travelmemories.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmemories.data.repository.MediaSmokeTestRepository
import com.example.travelmemories.data.repository.MediaSmokeTestResult
import com.example.travelmemories.ui.state.SmokeTestUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val mediaSmokeTestRepository: MediaSmokeTestRepository
) : ViewModel() {
    private val _smokeTestState = MutableStateFlow(
        SmokeTestUiState(status = "Running media storage smoke test…")
    )
    val smokeTestState: StateFlow<SmokeTestUiState> = _smokeTestState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mediaSmokeTestRepository.runSmokeTest()
            val uiState = when (result) {
                is MediaSmokeTestResult.Success -> SmokeTestUiState(
                    status = "Media storage ready",
                    details = "Reference: ${result.uri}"
                )
                is MediaSmokeTestResult.Failure -> SmokeTestUiState(
                    status = "Media storage error",
                    details = result.reason
                )
            }
            _smokeTestState.value = uiState
        }
    }
}
