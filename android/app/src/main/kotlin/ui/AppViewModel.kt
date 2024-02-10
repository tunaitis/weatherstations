package weatherstations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import weatherstations.data.SettingsStore

class AppViewModel(
    private val isSystemInDarkTheme: Boolean,
    private val settingsStore: SettingsStore
) : ViewModel() {

    private val _theme = MutableStateFlow("")
    val theme = _theme.asStateFlow()

    val isDarkTheme = _theme.map {
        if (it.isEmpty()) {
            isSystemInDarkTheme
        } else {
            it == "dark"
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        isSystemInDarkTheme
    )

    init {
        viewModelScope.launch {
            _theme.value = settingsStore.getTheme()
        }
    }

    fun setTheme(theme: String) {
        _theme.value = theme
        viewModelScope.launch {
            settingsStore.setTheme(theme)
        }
    }
}