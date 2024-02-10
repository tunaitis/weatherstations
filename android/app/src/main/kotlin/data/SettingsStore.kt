package weatherstations.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsStore(private val context: Context) {
    private val starredStationsKey = stringSetPreferencesKey("starredStations")
    private val homeRouteKey = stringPreferencesKey("homeRoute")
    private val themeKey = stringPreferencesKey("theme")

    suspend fun getStarredStations(): Set<String> {
        val data = context.dataStore.data.first()
        return data[starredStationsKey] ?: emptySet()
    }

    suspend fun setStarredStations(stations: Set<String>) {
        context.dataStore.edit { pref ->
            pref[starredStationsKey] = stations
        }
    }

    suspend fun getHomeRoute(): String {
        val data = context.dataStore.data.first()
        return data[homeRouteKey] ?: "stations"
    }

    suspend fun setHomeRoute(route: String) {
        context.dataStore.edit { pref ->
            pref[homeRouteKey] = route
        }
    }

    suspend fun getTheme(): String {
        val data = context.dataStore.data.first()
        return data[themeKey] ?: ""
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { pref ->
            pref[themeKey] = theme
        }
    }
}