package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.core.model.Game
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase service for managing games at courts
 * Games are stored as subcollection under each court
 */
@Singleton
class FirebaseGameService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val COURTS_COLLECTION = "courts"
        private const val GAMES_COLLECTION = "games"
    }

    /**
     * Get all games for a specific court in real-time
     */
    fun getGamesForCourt(courtId: String): Flow<List<Game>> = callbackFlow {
        val listener = firestore
            .collection(COURTS_COLLECTION)
            .document(courtId)
            .collection(GAMES_COLLECTION)
            .whereEqualTo("isAvailable", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val games = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Game::class.java)?.copy(id = doc.id)
                    }
                    trySend(games)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get a specific game by ID
     */
    suspend fun getGameById(courtId: String, gameId: String): Game? {
        return try {
            val document = firestore
                .collection(COURTS_COLLECTION)
                .document(courtId)
                .collection(GAMES_COLLECTION)
                .document(gameId)
                .get()
                .await()

            document.toObject(Game::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Add a new game to a court (Admin functionality)
     */
    suspend fun addGame(courtId: String, game: Game): Result<String> {
        return try {
            val docRef = firestore
                .collection(COURTS_COLLECTION)
                .document(courtId)
                .collection(GAMES_COLLECTION)
                .add(game)
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update game details (Admin functionality)
     */
    suspend fun updateGame(courtId: String, gameId: String, game: Game): Result<Unit> {
        return try {
            firestore
                .collection(COURTS_COLLECTION)
                .document(courtId)
                .collection(GAMES_COLLECTION)
                .document(gameId)
                .set(game)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Toggle game availability (Admin functionality)
     */
    suspend fun toggleGameAvailability(
        courtId: String,
        gameId: String,
        isAvailable: Boolean
    ): Result<Unit> {
        return try {
            firestore
                .collection(COURTS_COLLECTION)
                .document(courtId)
                .collection(GAMES_COLLECTION)
                .document(gameId)
                .update("isAvailable", isAvailable)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}