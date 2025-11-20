package com.project.gameonhai.court.data

import com.project.gameonhai.core.data.repository.GameRepository
import com.project.gameonhai.core.model.Game
import com.project.gameonhai.core.network.firebase.FirebaseGameService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of GameRepository using Firebase
 */
class GameRepositoryImpl @Inject constructor(
    private val gameService: FirebaseGameService
) : GameRepository {

    override fun getGamesForCourt(courtId: String): Flow<List<Game>> {
        return gameService.getGamesForCourt(courtId)
    }

    override suspend fun getGameById(courtId: String, gameId: String): Game? {
        return gameService.getGameById(courtId, gameId)
    }
}