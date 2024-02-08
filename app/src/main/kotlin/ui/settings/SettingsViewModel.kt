package weatherstations.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import weatherstations.data.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsStore: SettingsStore
) : ViewModel() {
    private val _homeRoute = MutableStateFlow("")
    val homeRoute = _homeRoute.asStateFlow()

    private val _locale = MutableStateFlow("")
    val locale = _locale.asStateFlow()

    init {
        viewModelScope.launch {
            _homeRoute.value = settingsStore.getHomeRoute()
            _locale.value = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        }
    }

    fun setHomeScreen(route: String) {
        _homeRoute.value = route
        viewModelScope.launch {
            settingsStore.setHomeRoute(route)
        }
    }

    fun setLocale(localeName: String) {
        _locale.value = localeName
        val appLocale = LocaleListCompat.forLanguageTags(localeName)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}