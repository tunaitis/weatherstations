package weatherstations.data

import weatherstations.models.Station
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StationsRepository(
    private val settingsStore: SettingsStore,
    private val stationsDataSource: StationsDataSource,
) {

    private val _allStations = MutableStateFlow<List<Station>>(emptyList())

    val allStations: StateFlow<List<Station>>
        get() = _allStations

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