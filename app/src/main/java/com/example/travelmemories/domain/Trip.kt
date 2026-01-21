package com.example.travelmemories.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

private fun LocalDate.formatDate(): String = format(dateFormatter)

private fun Instant.formatInstant(): String =
    atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)

data class Trip(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val coverImageUrl: String?,
    val archived: Boolean,
    val createdAt: Instant
) {
    val dateRangeLabel: String
        get() = if (endDate == null) {
            "${startDate.formatDate()} — ongoing"
        } else {
            "${startDate.formatDate()} — ${endDate.formatDate()}"
        }

    val createdAtLabel: String
        get() = createdAt.formatInstant()
}
