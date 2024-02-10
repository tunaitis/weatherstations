package weatherstations.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import weatherstations.models.Station
import weatherstations.ui.components.StationCard

@Composable
fun StationListScreen(
    stations: List<Station>,
    onStarClick: (id: String) -> Unit,
    onCameraClick: (id: String) -> Unit,
    onHistoryClick: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(stations) { station ->
            StationCard(
                station,
                onStarClick = onStarClick,
                onCameraClick = onCameraClick,
                onHistoryClick = onHistoryClick,
                modifier = Modifier.padding(15.dp),
            )
            Divider()
        }
    }
}
