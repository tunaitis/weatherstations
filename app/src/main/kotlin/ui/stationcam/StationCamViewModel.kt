package weatherstations.ui.stationcam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import weatherstations.data.Result
import weatherstations.data.StationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StationCamViewModel(
    private val stationsRepository: StationsRepository,
    val id: String,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val station = stationsRepository
        .allStations.map { stations -> stations.first { it.id == id } }

    private val _photo = MutableStateFlow("")
    val photo = _photo.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true

            val result = stationsRepository.getStationPhoto(id)
            if (result is Result.Success) {
                _photo.value = result.data
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