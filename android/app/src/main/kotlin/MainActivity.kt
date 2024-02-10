package weatherstations

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import weatherstations.ui.App
import weatherstations.ui.AppViewModel

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as WeatherStationsApplication

        setContent {
            enableEdgeToEdge()

            val isSystemInDarkTheme = isSystemInDarkTheme()
            val viewModel = AppViewModel(isSystemInDarkTheme, app.settingsStore)

            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))

            LaunchedEffect(true) {
                if (!permissionState.allPermissionsGranted) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }

            app.locationDataSource.updatePermissionStatus(permissionState.allPermissionsGranted)
            if (permissionState.allPermissionsGranted) {
                app.locationDataSource.updateLocation()
            }

            App(viewModel, app.settingsStore, app.stationsRepository, app.locationDataSource)
        }
    }
}
