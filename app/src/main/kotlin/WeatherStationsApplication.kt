package weatherstations

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import weatherstations.data.LocationDataSource
import weatherstations.data.SettingsStore
import weatherstations.data.StationsDataSource
import weatherstations.data.StationsRepository

class WeatherStationsApplication : Application() {
    lateinit var stationsRepository: StationsRepository
    lateinit var settingsStore: SettingsStore
    lateinit var locationDataSource: LocationDataSource

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()

        val appScope = CoroutineScope(SupervisorJob())

        settingsStore = SettingsStore(this)
        locationDataSource = LocationDataSource(this)
        stationsRepository = StationsRepository(settingsStore, StationsDataSource(), locationDataSource, appScope)
    }

}