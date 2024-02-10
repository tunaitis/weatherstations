package weatherstations.ui.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import weatherstations.data.ErrorType
import weatherstations.data.LocationDataSource
import weatherstations.data.SettingsStore
import weatherstations.data.StationsRepository
import weatherstations.models.Station

enum class StationListSort {
    Alphabetical,
    Distance
}

@OptIn(ExperimentalPermissionsApi::class)
class HomeViewModel(
    private val settingsStore: SettingsStore,
    private val stationsRepository: StationsRepository,
    private val locationDataSource: LocationDataSource
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error = _error.asStateFlow()

    private val _toast = MutableStateFlow("")
    val toast = _toast.asStateFlow()

    lateinit var homeRoute: String

    private val _sort = MutableStateFlow(StationListSort.Alphabetical)
    val sort = _sort.asStateFlow()

    private val _location = MutableStateFlow<Location?>(null)

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

    val sortedStations: StateFlow<List<Station>> = combine(stations, _sort) { stations, sort ->
        if (sort == StationListSort.Alphabetical) {
            stations.sortedBy { it.name }
        } else {
            stations.sortedBy { it.distance }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
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

    fun sort(value: StationListSort) {
        if (value == StationListSort.Distance) {
            locationDataSource.updateLocation()

            if (!locationDataSource.isPermissionGranted.value) {
                _error.value = ErrorType.LocationPermissionError
                return
            }

            if (!locationDataSource.isConnected.value) {
                _error.value = ErrorType.LocationDisabledError
                return
            }
        }

        _sort.value = value
    }

    fun clearError() {
        _error.value = null
    }
}
