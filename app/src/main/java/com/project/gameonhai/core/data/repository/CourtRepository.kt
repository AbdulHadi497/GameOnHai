package com.project.gameonhai.core.data.repository

import com.project.gameonhai.core.model.Court
import kotlinx.coroutines.flow.Flow

interface CourtRepository {
    fun getAllCourts(): Flow<List<Court>>
    fun getCourtById(courtId: String): Flow<Court?>
    fun searchCourts(query: String): Flow<List<Court>>
    fun getCourtsByCategory(categoryId: String): Flow<List<Court>>
}