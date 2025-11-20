package com.project.gameonhai.core.data.repository

import com.project.gameonhai.core.model.Game
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for game operations
 */
interface GameRepository {
    /**
     * Get all available games for a specific court
     */
    fun getGamesForCourt(courtId: String): Flow<List<Game>>

    /**
     * Get a specific game by ID
     */
    suspend fun getGameById(courtId: String, gameId: String): Game?
}