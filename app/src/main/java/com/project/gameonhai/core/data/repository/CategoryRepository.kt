package com.project.gameonhai.core.data.repository

import com.project.gameonhai.core.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
}