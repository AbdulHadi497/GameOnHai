package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.core.model.TimeSlot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase service for managing time slots
 * Time slots are stored as a separate collection
 */
@Singleton
class FirebaseTimeSlotService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TIMESLOTS_COLLECTION = "timeSlots"
    }

    /**
     * Get time slots for a specific game on a specific date
     * Returns real-time updates as slots get booked
     */
    fun getTimeSlotsForGameAndDate(
        courtId: String,
        gameId: String,
        date: String
    ): Flow<List<TimeSlot>> = callbackFlow {
        val listener = firestore
            .collection(TIMESLOTS_COLLECTION)
            .whereEqualTo("courtId", courtId)
            .whereEqualTo("gameId", gameId)
            .whereEqualTo("date", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val timeSlots = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(TimeSlot::class.java)?.copy(id = doc.id)
                    }.sortedBy { it.startTime } // Sort by start time

                    trySend(timeSlots)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get a specific time slot by ID
     */
    suspend fun getTimeSlotById(timeSlotId: String): TimeSlot? {
        return try {
            val document = firestore
                .collection(TIMESLOTS_COLLECTION)
                .document(timeSlotId)
                .get()
                .await()

            document.toObject(TimeSlot::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Mark time slot as booked
     * This is called when a booking is created
     */
    suspend fun markTimeSlotAsBooked(timeSlotId: String, bookingId: String): Result<Unit> {
        return try {
            firestore
                .collection(TIMESLOTS_COLLECTION)
                .document(timeSlotId)
                .update(
                    mapOf(
                        "isBooked" to true,
                        "bookingId" to bookingId
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mark time slot as available (when booking is cancelled)
     */
    suspend fun markTimeSlotAsAvailable(timeSlotId: String): Result<Unit> {
        return try {
            firestore
                .collection(TIMESLOTS_COLLECTION)
                .document(timeSlotId)
                .update(
                    mapOf(
                        "isBooked" to false,
                        "bookingId" to null
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create time slots for a game (Admin functionality)
     * Typically called when setting up a court's schedule
     */
    suspend fun createTimeSlots(timeSlots: List<TimeSlot>): Result<Unit> {
        return try {
            val batch = firestore.batch()

            timeSlots.forEach { timeSlot ->
                val docRef = firestore
                    .collection(TIMESLOTS_COLLECTION)
                    .document()
                batch.set(docRef, timeSlot)
            }

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Toggle time slot availability (Admin functionality)
     */
    suspend fun toggleTimeSlotAvailability(
        timeSlotId: String,
        isAvailable: Boolean
    ): Result<Unit> {
        return try {
            firestore
                .collection(TIMESLOTS_COLLECTION)
                .document(timeSlotId)
                .update("isAvailable", isAvailable)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete time slots for a specific date (Admin functionality)
     */
    suspend fun deleteTimeSlotsForDate(
        courtId: String,
        gameId: String,
        date: String
    ): Result<Unit> {
        return try {
            val querySnapshot = firestore
                .collection(TIMESLOTS_COLLECTION)
                .whereEqualTo("courtId", courtId)
                .whereEqualTo("gameId", gameId)
                .whereEqualTo("date", date)
                .get()
                .await()

            val batch = firestore.batch()
            querySnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}