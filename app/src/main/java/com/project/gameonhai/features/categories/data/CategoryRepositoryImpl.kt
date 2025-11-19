package com.project.gameonhai.categories.data

import com.project.gameonhai.core.data.repository.CategoryRepository
import com.project.gameonhai.core.model.Category
import com.project.gameonhai.core.network.firebase.FirebaseCategoryService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firebaseCategoryService: FirebaseCategoryService
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return firebaseCategoryService.getAllCategories()
    }
}