package weatherstations

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import weatherstations.ui.App
import weatherstations.ui.AppViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as WeatherStationsApplication

        setContent {
            enableEdgeToEdge()

            val isSystemInDarkTheme = isSystemInDarkTheme()
            val viewModel = AppViewModel(isSystemInDarkTheme, app.settingsStore)

            App(viewModel, app.settingsStore, app.stationsRepository)
        }
    }
}
