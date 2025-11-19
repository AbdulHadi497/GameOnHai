package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.core.model.Court
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCourtService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val courtsCollection = firestore.collection("courts")

    fun getAllCourts(): Flow<List<Court>> = callbackFlow {
        val listener = courtsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val courts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Court::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(courts)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getCourtById(courtId: String): Court? {
        return try {
            val doc = courtsCollection.document(courtId).get().await()
            doc.toObject(Court::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }

    fun searchCourts(query: String): Flow<List<Court>> = callbackFlow {
        val listener = courtsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val courts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Court::class.java)?.copy(id = doc.id)
                }?.filter {
                    it.name.contains(query, ignoreCase = true)
                } ?: emptyList()

                trySend(courts)
            }

        awaitClose { listener.remove() }
    }
}