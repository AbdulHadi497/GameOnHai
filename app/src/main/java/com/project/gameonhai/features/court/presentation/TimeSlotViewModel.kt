package com.project.gameonhai.feature_court.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.gameonhai.core.model.TimeSlot
import com.project.gameonhai.core.model.TimeSlotWithStatus
import com.project.gameonhai.feature_court.domain.GetTimeSlotsUseCase
import com.project.gameonhai.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class TimeSlotUiState(
    val timeSlots: List<TimeSlotWithStatus> = emptyList(),
    val selectedTimeSlot: TimeSlot? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TimeSlotViewModel @Inject constructor(
    private val getTimeSlotsUseCase: GetTimeSlotsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimeSlotUiState())
    val uiState: StateFlow<TimeSlotUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun selectTimeSlot(timeSlot: TimeSlot) {
        _uiState.update { it.copy(selectedTimeSlot = timeSlot) }
    }

    fun loadTimeSlots(courtId: String, gameId: String, date: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getTimeSlotsUseCase(courtId, gameId, date)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load time slots"
                        )
                    }
                }
                .collect { timeSlots ->
                    val currentTime = System.currentTimeMillis()
                    val timeSlotsWithStatus = timeSlots.map { timeSlot ->
                        TimeSlotWithStatus.fromTimeSlot(timeSlot, currentTime)
                    }

                    _uiState.update {
                        it.copy(
                            timeSlots = timeSlotsWithStatus,
                            isLoading = false
                        )
                    }
                }
        }
    }
}