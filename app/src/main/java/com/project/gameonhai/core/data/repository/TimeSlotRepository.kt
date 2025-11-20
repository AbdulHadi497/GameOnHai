package com.project.gameonhai.core.data.repository

import com.project.gameonhai.core.model.TimeSlot
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for time slot operations
 */
interface TimeSlotRepository {
    /**
     * Get time slots for a specific game on a specific date
     */
    fun getTimeSlotsForGameAndDate(
        courtId: String,
        gameId: String,
        date: String
    ): Flow<List<TimeSlot>>

    /**
     * Get a specific time slot by ID
     */
    suspend fun getTimeSlotById(timeSlotId: String): TimeSlot?
}