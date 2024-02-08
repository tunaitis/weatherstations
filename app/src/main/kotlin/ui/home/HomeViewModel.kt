package weatherstations.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import weatherstations.data.ErrorType
import weatherstations.data.SettingsStore
import weatherstations.data.StationsRepository
import weatherstations.models.Station
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val settingsStore: SettingsStore,
    private val stationsRepository: StationsRepository
) : ViewModel() {

    // val stations = stationsRepository.stations

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error = _error.asStateFlow()

    private val _isDropdownMenuExpanded = MutableStateFlow(false)
    val isDropdownMenuExpanded = _isDropdownMenuExpanded.asStateFlow()

    lateinit var homeRoute: String

    init {
        refreshStations()
    }

    val stations = stationsRepository.allStations
    private val _selectedStationId = MutableStateFlow("")
    val selectedStation: StateFlow<Station?> = _selectedStationId.combine(stations) { id, stations ->
        if (id.isBlank()) {
            null
        } else {
            stations.first { it.id == id}
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun refreshStations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = stationsRepository.refreshStations()
            homeRoute = settingsStore.getHomeRoute()
            _isLoading.value = false
        }
    }

    fun toggleStar(id: String) {
        viewModelScope.launch {
            stationsRepository.toggleStar(id)
        }
    }

    fun selectStation(id: String) {
        _selectedStationId.value = id
    }

    fun deselectStation() {
        selectStation("")
    }

    fun expandDropdown() {
        _isDropdownMenuExpanded.value = true
    }

    fun collapseDropdownMenu() {
        _isDropdownMenuExpanded.value = false
    }
}
