package com.project.gameonhai.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtbooking.core.model.Category
import com.courtbooking.core.model.Court
import com.courtbooking.features.home.domain.GetCategoriesUseCase
import com.courtbooking.features.home.domain.GetCourtsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val courts: List<Court> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCourtsUseCase: GetCourtsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load categories
            getCategoriesUseCase().collect { categories ->
                _uiState.value = _uiState.value.copy(
                    categories = categories
                )
            }
        }

        viewModelScope.launch {
            // Load courts
            getCourtsUseCase().collect { courts ->
                _uiState.value = _uiState.value.copy(
                    courts = courts,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        viewModelScope.launch {
            if (query.isBlank()) {
                getCourtsUseCase().collect { courts ->
                    _uiState.value = _uiState.value.copy(courts = courts)
                }
            } else {
                getCourtsUseCase.search(query).collect { courts ->
                    _uiState.value = _uiState.value.copy(courts = courts)
                }
            }
        }
    }

    fun onCategorySelected(categoryId: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = if (_uiState.value.selectedCategory == categoryId) null else categoryId
        )
    }
}