package com.project.gameonhai.core.model

/**
 * Represents a bookable time slot for a specific game at a court
 * Time slots are generated per day for each game
 */
data class TimeSlot(
    val id: String = "",
    val courtId: String = "",
    val gameId: String = "",
    val startTime: String = "", // Format: "09:00"
    val endTime: String = "", // Format: "10:00"
    val date: String = "", // Format: "2024-11-20"
    val isBooked: Boolean = false,
    val isAvailable: Boolean = true, // Admin can manually disable slots
    val bookingId: String? = null,
    val price: Double = 0.0 // Calculated from game price
)