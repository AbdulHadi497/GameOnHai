package com.project.gameonhai.core.model
/**
 * Helper enum for time slot status
 */
enum class TimeSlotStatus {
    AVAILABLE,
    BOOKED,
    UNAVAILABLE,
    PAST // Slot time has already passed
}

/**
 * UI state representation with computed status
 */
data class TimeSlotWithStatus(
    val timeSlot: TimeSlot,
    val status: TimeSlotStatus
) {
    companion object {
        fun fromTimeSlot(
            timeSlot: TimeSlot,
            currentDateTime: Long
        ): TimeSlotWithStatus {
            val status = when {
                !timeSlot.isAvailable -> TimeSlotStatus.UNAVAILABLE
                timeSlot.isBooked -> TimeSlotStatus.BOOKED
                isSlotInPast(timeSlot.date, timeSlot.startTime, currentDateTime) -> TimeSlotStatus.PAST
                else -> TimeSlotStatus.AVAILABLE
            }
            return TimeSlotWithStatus(timeSlot, status)
        }

        private fun isSlotInPast(date: String, startTime: String, currentDateTime: Long): Boolean {
            return try {
                val dateTimeString = "$date $startTime"
                val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                val slotDateTime = format.parse(dateTimeString)?.time ?: 0L
                slotDateTime < currentDateTime
            } catch (e: Exception) {
                false
            }
        }
    }
}