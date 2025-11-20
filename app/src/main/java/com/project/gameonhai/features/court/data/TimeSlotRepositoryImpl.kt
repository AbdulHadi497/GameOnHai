package com.project.gameonhai.court.data

import com.project.gameonhai.core.data.repository.TimeSlotRepository
import com.project.gameonhai.core.model.TimeSlot
import com.project.gameonhai.core.network.firebase.FirebaseTimeSlotService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of TimeSlotRepository using Firebase
 */
class TimeSlotRepositoryImpl @Inject constructor(
    private val timeSlotService: FirebaseTimeSlotService
) : TimeSlotRepository {

    override fun getTimeSlotsForGameAndDate(
        courtId: String,
        gameId: String,
        date: String
    ): Flow<List<TimeSlot>> {
        return timeSlotService.getTimeSlotsForGameAndDate(courtId, gameId, date)
    }

    override suspend fun getTimeSlotById(timeSlotId: String): TimeSlot? {
        return timeSlotService.getTimeSlotById(timeSlotId)
    }
}