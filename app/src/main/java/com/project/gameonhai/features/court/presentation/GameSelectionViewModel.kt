package com.project.gameonhai.feature_court.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.gameonhai.core.model.Game
import com.project.gameonhai.feature_court.domain.GetGamesForCourtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameSelectionUiState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GameSelectionViewModel @Inject constructor(
    private val getGamesForCourtUseCase: GetGamesForCourtUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameSelectionUiState())
    val uiState: StateFlow<GameSelectionUiState> = _uiState.asStateFlow()

    fun loadGames(courtId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getGamesForCourtUseCase(courtId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load games"
                        )
                    }
                }
                .collect { games ->
                    _uiState.update {
                        it.copy(
                            games = games,
                            isLoading = false
                        )
                    }
                }
        }
    }
}