package com.project.gameonhai.feature_court.domain


import com.project.gameonhai.core.data.repository.CourtRepository
import com.project.gameonhai.core.data.repository.GameRepository
import com.project.gameonhai.core.data.repository.TimeSlotRepository
import com.project.gameonhai.core.model.Court
import com.project.gameonhai.core.model.Game
import com.project.gameonhai.core.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get court details by ID
 */
class GetCourtDetailsUseCase @Inject constructor(
    private val courtRepository: CourtRepository
) {
    suspend operator fun invoke(courtId: String): Flow<Court?> {
        return courtRepository.getCourtById(courtId)
    }
}

/**
 * Use case to get all games available at a court
 */
class GetGamesForCourtUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(courtId: String): Flow<List<Game>> {
        return gameRepository.getGamesForCourt(courtId)
    }
}

/**
 * Use case to get time slots for a specific game and date
 */
class GetTimeSlotsUseCase @Inject constructor(
    private val timeSlotRepository: TimeSlotRepository
) {
    operator fun invoke(
        courtId: String,
        gameId: String,
        date: String
    ): Flow<List<TimeSlot>> {
        return timeSlotRepository.getTimeSlotsForGameAndDate(courtId, gameId, date)
    }
}