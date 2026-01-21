package com.example.travelmemories.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmemories.data.repository.TripRepository
import com.example.travelmemories.domain.Trip
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TripCreationResult {
    data object Success : TripCreationResult()
    data object DuplicateName : TripCreationResult()
    data object InvalidName : TripCreationResult()
}

class TripLibraryViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {
    val trips: StateFlow<List<Trip>> = tripRepository.observeTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _activeTripId = MutableStateFlow<Long?>(null)
    val activeTripId: StateFlow<Long?> = _activeTripId.asStateFlow()

    fun setActiveTrip(tripId: Long) {
        _activeTripId.value = tripId
    }

    fun archiveTrip(tripId: Long) {
        viewModelScope.launch {
            tripRepository.archiveTrip(tripId)
            if (_activeTripId.value == tripId) {
                _activeTripId.value = null
            }
        }
    }

    fun restoreTrip(tripId: Long) {
        viewModelScope.launch {
            tripRepository.restoreTrip(tripId)
        }
    }

    fun createTrip(
        name: String,
        startDate: LocalDate,
        endDate: LocalDate?,
        coverImageUrl: String?,
        onResult: (TripCreationResult) -> Unit
    ) {
        viewModelScope.launch {
            val trimmedName = name.trim()
            if (trimmedName.isBlank()) {
                onResult(TripCreationResult.InvalidName)
                return@launch
            }
            if (tripRepository.hasTripNamed(trimmedName)) {
                onResult(TripCreationResult.DuplicateName)
                return@launch
            }
            tripRepository.createTrip(trimmedName, startDate, endDate, coverImageUrl)
            onResult(TripCreationResult.Success)
        }
    }
}
