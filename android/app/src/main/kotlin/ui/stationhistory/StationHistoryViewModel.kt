package weatherstations.ui.stationhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import weatherstations.data.Result
import weatherstations.data.StationsRepository
import weatherstations.models.Station

class StationHistoryViewModel(
    private val stationsRepository: StationsRepository,
    val id: String,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val station = stationsRepository
        .allStations.map { stations -> stations.first { it.id == id } }

    private val _history = MutableStateFlow(emptyList<Station>())
    val history = _history.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true

            val result = stationsRepository.getHistoricalData(id)
            if (result is Result.Success) {
                _history.value = result.data
            }

            _isLoading.value = false
        }
    }

    fun toggleStar(id: String) {
        viewModelScope.launch {
            stationsRepository.toggleStar(id)
        }
    }
}