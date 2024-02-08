package weatherstations

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import weatherstations.data.SettingsStore
import weatherstations.data.StationsDataSource
import weatherstations.data.StationsRepository

class WeatherStationsApplication : Application() {
    lateinit var stationsRepository: StationsRepository
    lateinit var settingsStore: SettingsStore

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        settingsStore = SettingsStore(this)
        stationsRepository = StationsRepository(settingsStore, StationsDataSource())
    }
}