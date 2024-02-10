package weatherstations.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import weatherstations.models.Station

@Composable
fun StationMapScreen(stations: List<Station>, onMarkerClick: (id: String) -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.1735, 23.8948), 6.5f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        stations.forEach { station ->
            Marker(
                state = MarkerState(
                    position = LatLng(station.latitude, station.longitude),
                ),
                onClick = {
                    onMarkerClick(station.id)
                    false
                },
                // title = it.name,
                // snippet = it.road,
            )
        }
    }
}