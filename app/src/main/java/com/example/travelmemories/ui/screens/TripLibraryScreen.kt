package com.example.travelmemories.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travelmemories.domain.Trip
import com.example.travelmemories.ui.viewmodel.TripCreationResult
import com.example.travelmemories.ui.viewmodel.TripLibraryViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripLibraryScreen(
    viewModel: TripLibraryViewModel,
    onOpenTrip: (Long) -> Unit
) {
    val trips by viewModel.trips.collectAsState()
    val activeTripId by viewModel.activeTripId.collectAsState()
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Library") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Create trip")
            }
        }
    ) { paddingValues ->
        TripLibraryContent(
            trips = trips,
            activeTripId = activeTripId,
            onOpenTrip = onOpenTrip,
            onArchiveTrip = { viewModel.archiveTrip(it) },
            onRestoreTrip = { viewModel.restoreTrip(it) },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }

    if (showCreateDialog) {
        CreateTripDialog(
            onDismiss = { showCreateDialog = false },
            onCreateTrip = { name, startDate, endDate, coverImage, onResult ->
                viewModel.createTrip(name, startDate, endDate, coverImage) { result ->
                    onResult(result)
                    if (result == TripCreationResult.Success) {
                        showCreateDialog = false
                    }
                }
            }
        )
    }
}

@Composable
private fun TripLibraryContent(
    trips: List<Trip>,
    activeTripId: Long?,
    onOpenTrip: (Long) -> Unit,
    onArchiveTrip: (Long) -> Unit,
    onRestoreTrip: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTrip = trips.firstOrNull { it.id == activeTripId }
    val activeTrips = trips.filter { !it.archived }
    val archivedTrips = trips.filter { it.archived }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (activeTrip != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Active Trip",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = activeTrip.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = activeTrip.dateRangeLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (trips.isEmpty()) {
            EmptyTripLibraryState()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 72.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (activeTrips.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Your Trips")
                    }
                    items(activeTrips, key = { it.id }) { trip ->
                        TripCard(
                            trip = trip,
                            isActive = trip.id == activeTripId,
                            onOpenTrip = { onOpenTrip(trip.id) },
                            onArchiveTrip = { onArchiveTrip(trip.id) }
                        )
                    }
                }
                if (archivedTrips.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Archived")
                    }
                    items(archivedTrips, key = { it.id }) { trip ->
                        TripCard(
                            trip = trip,
                            isActive = false,
                            onRestoreTrip = { onRestoreTrip(trip.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTripLibraryState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "No trips yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Create your first trip to start collecting memories.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun TripCard(
    trip: Trip,
    isActive: Boolean,
    onOpenTrip: (() -> Unit)? = null,
    onArchiveTrip: (() -> Unit)? = null,
    onRestoreTrip: (() -> Unit)? = null
) {
    Card(
        colors = if (isActive) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.TravelExplore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = trip.dateRangeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isActive) {
                    Text(
                        text = "Active",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                onOpenTrip?.let {
                    Button(onClick = it) {
                        Text("Open")
                    }
                }
                onArchiveTrip?.let {
                    FilledTonalButton(onClick = it) {
                        Icon(Icons.Filled.Archive, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Archive")
                    }
                }
                onRestoreTrip?.let {
                    FilledTonalButton(onClick = it) {
                        Icon(Icons.Filled.Restore, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Restore")
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateTripDialog(
    onDismiss: () -> Unit,
    onCreateTrip: (String, LocalDate, LocalDate?, String?, (TripCreationResult) -> Unit) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var startDateInput by rememberSaveable { mutableStateOf("") }
    var endDateInput by rememberSaveable { mutableStateOf("") }
    var coverImageInput by rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Trip") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Trip name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = startDateInput,
                    onValueChange = { startDateInput = it },
                    label = { Text("Start date (YYYY-MM-DD)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = endDateInput,
                    onValueChange = { endDateInput = it },
                    label = { Text("End date (optional)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = coverImageInput,
                    onValueChange = { coverImageInput = it },
                    label = { Text("Cover image URL (optional)") },
                    singleLine = true
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val startDate = parseDate(startDateInput)
                    val endDate = parseDate(endDateInput)
                    if (name.isBlank()) {
                        errorMessage = "Trip name is required."
                        return@TextButton
                    }
                    if (startDate == null) {
                        errorMessage = "Start date is required (YYYY-MM-DD)."
                        return@TextButton
                    }
                    if (endDateInput.isNotBlank() && endDate == null) {
                        errorMessage = "End date must be a valid date (YYYY-MM-DD)."
                        return@TextButton
                    }
                    if (endDate != null && endDate.isBefore(startDate)) {
                        errorMessage = "End date must be after the start date."
                        return@TextButton
                    }
                    onCreateTrip(
                        name.trim(),
                        startDate,
                        endDate,
                        coverImageInput.trim().ifBlank { null }
                    ) { result ->
                        errorMessage = when (result) {
                            TripCreationResult.Success -> null
                            TripCreationResult.DuplicateName ->
                                "Trip name already exists. Choose another name."
                            TripCreationResult.InvalidName ->
                                "Trip name is required."
                        }
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun parseDate(input: String): LocalDate? {
    if (input.isBlank()) return null
    return try {
        LocalDate.parse(input.trim())
    } catch (exception: DateTimeParseException) {
        null
    }
}
