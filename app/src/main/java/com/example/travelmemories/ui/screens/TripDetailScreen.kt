package com.example.travelmemories.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travelmemories.ui.viewmodel.TripLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    tripId: Long,
    viewModel: TripLibraryViewModel,
    onNavigateBack: () -> Unit,
    onArchiveTrip: () -> Unit
) {
    val trips by viewModel.trips.collectAsState()
    val trip = trips.firstOrNull { it.id == tripId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.name ?: "Trip") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (trip == null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Trip not found",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = onNavigateBack) {
                    Text("Return to Trip Library")
                }
            }
        } else {
            TripDetailContent(
                tripName = trip.name,
                tripDates = trip.dateRangeLabel,
                coverImage = trip.coverImageUrl,
                createdAt = trip.createdAtLabel,
                paddingValues = paddingValues,
                onArchiveTrip = onArchiveTrip
            )
        }
    }
}

@Composable
private fun TripDetailContent(
    tripName: String,
    tripDates: String,
    coverImage: String?,
    createdAt: String,
    paddingValues: PaddingValues,
    onArchiveTrip: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Active Trip",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = tripName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = tripDates,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        coverImage?.let {
            Text(
                text = "Cover image: $it",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "Created $createdAt",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onArchiveTrip) {
                Icon(Icons.Filled.Archive, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Archive Trip")
            }
        }
    }
}
