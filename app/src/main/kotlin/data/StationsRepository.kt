package weatherstations.data

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import weatherstations.models.Station

class StationsRepository(
    private val settingsStore: SettingsStore,
    private val stationsDataSource: StationsDataSource,
    locationService: LocationDataSource,
    externalScope: CoroutineScope
) {

    private val _allStations = MutableStateFlow<List<Station>>(emptyList())

    val allStations: StateFlow<List<Station>> = combine(_allStations, locationService.lastLocation) { stations, location ->
        if (location == null) {
            stations
        } else {
            stations.map { station ->
                var r = floatArrayOf(0.0f)
                Location.distanceBetween(location.latitude, location.longitude, station.latitude, station.longitude, r)
                station.distance = r[0]
                station
            }
        }
    }.stateIn(
        externalScope,
        SharingStarted.WhileSubscribed(5000),
        _allStations.value
    )

    suspend fun refreshStations(): ErrorType? {
        val starredStations = settingsStore.getStarredStations()
        val result = stationsDataSource.getStations()

        if (result is Result.Success) {
            _allStations.emit(result.data.map {
                if (starredStations.contains(it.id)) {
                    it.copy(isStarred = true)
                } else {
                    it
                }
            })

            return null
        }

        return (result as Result.Error).error
    }

    suspend fun getStations(): Result<List<Station>> {
        val starredStations = settingsStore.getStarredStations()
        val result = stationsDataSource.getStations()

        if (result is Result.Success) {
            _allStations.emit(result.data.map {
                if (starredStations.contains(it.id)) {
                    it.copy(isStarred = true)
                } else {
                    it
                }
            })

        }

        return result
    }

    suspend fun getStationPhoto(id: String): Result<String> {
        return stationsDataSource.getStationPhoto(id)
    }

    suspend fun getHistoricalData(id: String): Result<List<Station>> {
        return stationsDataSource.getHistoricalData(id)
    }

    suspend fun toggleStar(id: String) {
        _allStations.update { stations ->
            val updated = stations.map {
                if (it.id == id) {
                    it.copy(isStarred = !it.isStarred)
                } else {
                    it
                }
            }

            val starredStation = updated.filter { it.isStarred }.map { it.id }.toSet()
            settingsStore.setStarredStations(starredStation)

            updated
        }
    }
}