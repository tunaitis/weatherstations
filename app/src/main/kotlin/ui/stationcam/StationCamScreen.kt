package weatherstations.ui.stationcam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import weatherstations.R
import weatherstations.models.emptyStation
import weatherstations.ui.components.ProgressIndicator
import weatherstations.ui.components.StationHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationCamScreen(
    viewModel: StationCamViewModel,
    onBackClick: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val station by viewModel.station.collectAsState(emptyStation())
    val photo by viewModel.photo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    StationHeader(
                        station = station,
                        onStarClick = viewModel::toggleStar,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        Box (
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                ProgressIndicator()
            } else {
                if (photo != "") {
                    AsyncImage(
                        model = photo,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.unable_to_load_photo),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
