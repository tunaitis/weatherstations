package weatherstations.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import weatherstations.data.StationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val stationsRepository: StationsRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _stations = stationsRepository.allStations
    val stations = searchText
        .combine(_stations) { text, stations ->
            if (text.isBlank()) {
                stations
            } else {
                stations.filter { it.name.contains(text, ignoreCase = true) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _stations.value
        )

    init {
        /*
        viewModelScope.launch {
            stationsRepository.loadStations()
        }*/
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun toggleStar(id: String) {
        viewModelScope.launch {
             stationsRepository.toggleStar(id)
        }
    }
}