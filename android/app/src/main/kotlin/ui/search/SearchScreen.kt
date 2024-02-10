package weatherstations.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import weatherstations.R
import weatherstations.ui.components.SimpleStationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, onBackClick: () -> Unit) {
    val searchText by viewModel.searchText.collectAsState()
    val stations = viewModel.stations.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchText,
                        onValueChange = viewModel::onSearchTextChange,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text(stringResource(id = R.string.search)) },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(stations) { station ->
                SimpleStationCard(station = station, onStar = { viewModel.toggleStar(station.id) })
                Divider()
            }
        }
    }
}