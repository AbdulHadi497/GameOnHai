package com.project.gameonhai.core.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.core.model.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseCategoryService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val categoriesCollection = firestore.collection("categories")

    fun getAllCategories(): Flow<List<Category>> = callbackFlow {
        val listener = categoriesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Category::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(categories)
            }

        awaitClose { listener.remove() }
    }
}