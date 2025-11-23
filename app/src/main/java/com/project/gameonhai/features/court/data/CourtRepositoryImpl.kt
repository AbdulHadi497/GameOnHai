package com.project.gameonhai.features.court.data

import com.project.gameonhai.core.data.repository.CourtRepository
import com.project.gameonhai.core.model.Court
import com.project.gameonhai.core.network.firebase.FirebaseCourtService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CourtRepositoryImpl @Inject constructor(
    private val firebaseCourtService: FirebaseCourtService
) : CourtRepository {

    override fun getAllCourts(): Flow<List<Court>> {
        return firebaseCourtService.getAllCourts()
    }

    override fun getCourtById(courtId: String): Flow<Court?> {
        return firebaseCourtService.getCourtById(courtId)
    }

    override fun searchCourts(query: String): Flow<List<Court>> {
        return firebaseCourtService.searchCourts(query)
    }

    override fun getCourtsByCategory(categoryId: String): Flow<List<Court>> {
        TODO("Not implemented yet")
    }
}
