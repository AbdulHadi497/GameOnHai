package com.project.gameonhai.features.court.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.gameonhai.core.model.Court
import com.project.gameonhai.core.model.Game
import com.project.gameonhai.feature_court.domain.GetCourtDetailsUseCase
import com.project.gameonhai.feature_court.domain.GetGamesForCourtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CourtDetailsUiState(
    val court: Court? = null,
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CourtDetailsViewModel @Inject constructor(
    private val getCourtDetailsUseCase: GetCourtDetailsUseCase,
    private val getGamesForCourtUseCase: GetGamesForCourtUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtDetailsUiState())
    val uiState: StateFlow<CourtDetailsUiState> = _uiState.asStateFlow()

    fun loadCourtDetails(courtId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Load court details
                val court = getCourtDetailsUseCase(courtId)

                if (court == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Court not found"
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(court = court as Court) }

                // Load games for this court
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

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load court details"
                    )
                }
            }
        }
    }
}