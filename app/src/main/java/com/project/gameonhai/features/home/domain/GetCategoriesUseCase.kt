package com.project.gameonhai.feature_home.domain

import com.project.gameonhai.core.data.repository.CategoryRepository
import com.project.gameonhai.core.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAllCategories()
    }
}