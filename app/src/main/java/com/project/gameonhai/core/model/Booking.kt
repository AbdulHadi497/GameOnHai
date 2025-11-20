package com.project.gameonhai.core.model

/**
 * Represents a booking made by a user
 * No user account needed - identified by phone number
 */
data class Booking(
    val id: String = "",
    val courtId: String = "",
    val courtName: String = "",
    val gameId: String = "",
    val gameName: String = "",
    val timeSlotId: String = "",
    val teamName: String = "",
    val phoneNumber: String = "", // Primary identifier for user
    val startTime: String = "", // "09:00"
    val endTime: String = "", // "10:00"
    val date: String = "", // "2024-11-20"
    val totalPrice: Double = 0.0,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val createdAt: Long = System.currentTimeMillis(),
    val cancelledAt: Long? = null,
    val cancellationReason: String? = null
)

/**
 * Booking status enum
 */
enum class BookingStatus(val value: String) {
    CONFIRMED("confirmed"),
    CANCELLED("cancelled"),
    COMPLETED("completed"); // Automatically set after slot time passes

    companion object {
        fun fromString(value: String): BookingStatus {
            return entries.find { it.value == value } ?: CONFIRMED
        }
    }
}

/**
 * Request model for creating a booking
 */
data class CreateBookingRequest(
    val courtId: String,
    val courtName: String,
    val gameId: String,
    val gameName: String,
    val timeSlotId: String,
    val teamName: String,
    val phoneNumber: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val totalPrice: Double
)

/**
 * UI state for booking details display
 */
data class BookingDetails(
    val booking: Booking,
    val canCancel: Boolean // True if >30 minutes before start time
)