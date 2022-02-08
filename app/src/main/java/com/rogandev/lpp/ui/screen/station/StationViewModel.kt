package com.rogandev.lpp.ui.screen.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogandev.lpp.repository.BusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(
    private val busRepository: BusRepository,
) : ViewModel() {

    private val stationCodeFlow = MutableStateFlow("")

    private val _uiStateFlow = MutableStateFlow(StationScreenState(emptyList(), emptyList(), true))
    val uiStateFlow get() = _uiStateFlow.asStateFlow()

    init {
        stationCodeFlow.filter { it.isNotBlank() }.mapLatest { code ->

            while (true) {
                _uiStateFlow.update { it.copy(loading = true) }
                delay(1000)

                busRepository.getStationMessages(code).onSuccess { messages ->
                    _uiStateFlow.update { it.copy(messages = messages, loading = false) }
                }.onFailure { err ->
                    Timber.e(err)
                    _uiStateFlow.update { it.copy(loading = false) }
                }

                delay(15_000)
            }

        }.launchIn(viewModelScope)
    }

    fun setStationCode(code: String) {
        stationCodeFlow.value = code
    }
}

data class StationScreenState(
    val messages: List<String>,
    val arrivals: List<String>,
    val loading: Boolean,
)
