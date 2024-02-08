package weatherstations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import weatherstations.R
import weatherstations.models.Station
import weatherstations.ui.theme.WeatherStationsTheme


@Composable
fun RowScope.StationCardProp(name: String, value: String) {
    Card (
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(1f)
    ) {
        Column (
            modifier = Modifier
                // .fillMaxWidth()
                //.weight(1f)
                .padding(10.dp, 10.dp),

        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "$value",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun SimpleStationCard(station: Station, onStar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 20.dp, 15.dp, 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = station.road,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(0.7f),
                )
            }
            IconButton(onClick = onStar) {
                if (station.isStarred) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        Icons.Outlined.StarOutline,
                        contentDescription = null,
                        modifier = Modifier.alpha(0.5f),
                        //tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun StationProps(station: Station, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            StationCardProp(name = stringResource(R.string.temperature), value = "${station.temperature} °C")
            StationCardProp(name = stringResource(R.string.precipitation), value = "${station.precipitation} mm")
            StationCardProp(name = stringResource(R.string.road_surface), value = "${station.roadSurface}")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            StationCardProp(name = stringResource(R.string.wind), value = "${station.windAverage} m/s")
            StationCardProp(name = stringResource(R.string.wind_max), value = "${station.windMax} m/s")
            StationCardProp(name = stringResource(R.string.wind_direction), value = "${station.windDirection}")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            StationCardProp(name = stringResource(R.string.visibility), value = "${station.visibility} m")
            StationCardProp(name = stringResource(R.string.dew_point), value = "${station.dewPoint} °C")
            StationCardProp(name = stringResource(R.string.road_surface), value = "${station.roadSurface}")
        }
    }
}

@Composable
fun StationHeader(
    station: Station,
    onStarClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = station.road,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.7f),
            )
        }
        Row {
            IconButton(onClick = { onStarClick(station.id) }) {
                if (station.isStarred) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        Icons.Outlined.StarOutline,
                        contentDescription = null,
                        //tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun StationCard(
    station: Station,
    onStarClick: (id: String) -> Unit,
    onCameraClick: (id: String) -> Unit,
    onHistoryClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        StationHeader(station = station, onStarClick = onStarClick)
        Spacer(modifier = Modifier.height(20.dp))
        StationProps(station)
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.updated),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.alpha(0.7f),
                )
                Text(
                    text = station.updated,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(0.7f),
                )
            }
            Row {
                IconButton(onClick = { onHistoryClick(station.id) }) {
                    Icon(
                        Icons.Outlined.History,
                        contentDescription = null,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
                IconButton(onClick = { onCameraClick(station.id) }) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, apiLevel = 30)
@Composable
fun StationCardPreview() {
    val station = Station(
        id = "1",
        name = "Test Station",
        road = "Vilnius-Kaunas-Klaipėda",
    )
    WeatherStationsTheme {
    }
}