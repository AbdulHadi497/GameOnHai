package com.project.gameonhai.feature_home.domain

import com.project.gameonhai.core.data.repository.CourtRepository
import com.project.gameonhai.core.model.Court
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCourtsUseCase @Inject constructor(
    private val courtRepository: CourtRepository
) {
    operator fun invoke(): Flow<List<Court>> {
        return courtRepository.getAllCourts()
    }

    fun search(query: String): Flow<List<Court>> {
        return courtRepository.searchCourts(query)
    }
}