package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.core.model.Court
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseCourtService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val courtsCollection = firestore.collection("Court")

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

    fun getCourtById(courtId: String): Flow<Court?> = callbackFlow {
        val docRef = courtsCollection.document(courtId)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val court = snapshot?.toObject(Court::class.java)?.copy(id = snapshot.id)
            trySend(court)
        }
        awaitClose { listener.remove() }
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
