package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.gameonhai.core.model.Booking
import com.project.gameonhai.core.model.BookingStatus
import com.project.gameonhai.core.model.CreateBookingRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase service for managing bookings
 */
@Singleton
class FirebaseBookingService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val BOOKINGS_COLLECTION = "bookings"
    }

    /**
     * Create a new booking
     * Returns the booking ID on success
     */
    suspend fun createBooking(request: CreateBookingRequest): Result<String> {
        return try {
            val booking = Booking(
                courtId = request.courtId,
                courtName = request.courtName,
                gameId = request.gameId,
                gameName = request.gameName,
                timeSlotId = request.timeSlotId,
                teamName = request.teamName,
                phoneNumber = request.phoneNumber,
                startTime = request.startTime,
                endTime = request.endTime,
                date = request.date,
                totalPrice = request.totalPrice,
                status = BookingStatus.CONFIRMED,
                createdAt = System.currentTimeMillis()
            )

            val docRef = firestore
                .collection(BOOKINGS_COLLECTION)
                .add(booking)
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get a specific booking by ID
     */
    suspend fun getBookingById(bookingId: String): Booking? {
        return try {
            val document = firestore
                .collection(BOOKINGS_COLLECTION)
                .document(bookingId)
                .get()
                .await()

            document.toObject(Booking::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get all bookings for a specific phone number
     * Used by normal users to view their bookings
     */
    fun getBookingsByPhoneNumber(phoneNumber: String): Flow<List<Booking>> = callbackFlow {
        val listener = firestore
            .collection(BOOKINGS_COLLECTION)
            .whereEqualTo("phoneNumber", phoneNumber)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val bookings = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Booking::class.java)?.copy(id = doc.id)
                    }
                    trySend(bookings)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get all bookings for a specific court
     * Used by court admins to view their court's bookings
     */
    fun getBookingsByCourt(courtId: String): Flow<List<Booking>> = callbackFlow {
        val listener = firestore
            .collection(BOOKINGS_COLLECTION)
            .whereEqualTo("courtId", courtId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val bookings = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Booking::class.java)?.copy(id = doc.id)
                    }
                    trySend(bookings)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get all bookings (Super Admin functionality)
     */
    fun getAllBookings(): Flow<List<Booking>> = callbackFlow {
        val listener = firestore
            .collection(BOOKINGS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val bookings = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Booking::class.java)?.copy(id = doc.id)
                    }
                    trySend(bookings)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Cancel a booking
     * Updates booking status and returns cancellation timestamp
     */
    suspend fun cancelBooking(
        bookingId: String,
        reason: String? = null
    ): Result<Long> {
        return try {
            val cancelledAt = System.currentTimeMillis()

            firestore
                .collection(BOOKINGS_COLLECTION)
                .document(bookingId)
                .update(
                    mapOf(
                        "status" to BookingStatus.CANCELLED.value,
                        "cancelledAt" to cancelledAt,
                        "cancellationReason" to reason
                    )
                )
                .await()

            Result.success(cancelledAt)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mark booking as completed (automatically after time slot passes)
     */
    suspend fun markBookingAsCompleted(bookingId: String): Result<Unit> {
        return try {
            firestore
                .collection(BOOKINGS_COLLECTION)
                .document(bookingId)
                .update("status", BookingStatus.COMPLETED.value)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get bookings for a specific date and court (Admin functionality)
     */
    suspend fun getBookingsForCourtAndDate(
        courtId: String,
        date: String
    ): List<Booking> {
        return try {
            val querySnapshot = firestore
                .collection(BOOKINGS_COLLECTION)
                .whereEqualTo("courtId", courtId)
                .whereEqualTo("date", date)
                .whereEqualTo("status", BookingStatus.CONFIRMED.value)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Booking::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}