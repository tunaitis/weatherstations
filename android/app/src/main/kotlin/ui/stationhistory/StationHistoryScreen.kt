package weatherstations.ui.stationhistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import weatherstations.R
import weatherstations.models.Station
import weatherstations.models.emptyStation
import weatherstations.ui.components.ProgressIndicator
import weatherstations.ui.components.StationHeader
import weatherstations.ui.components.WindDirection
import java.text.SimpleDateFormat

data class TabItem(
    val title: String,
)

@Composable
fun TableRow(containerColor: Color = Color.Transparent, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .padding(10.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun StrippedTableRow(index: Int, content: @Composable RowScope.() -> Unit) {
    TableRow(if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent, content)
}

@Composable
fun TemperatureTable(history: List<Station>, state: LazyListState) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        itemsIndexed(history) { i, item ->
            if (i == 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.temperature),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.dew_point),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Divider()
            }
            if (item.id == "" && i > 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                    )
                }
            } else {
                TableRow {
                    Text(
                        text = SimpleDateFormat("HH:mm").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                    )
                    Text(
                        text = "${item.temperature} °C",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(.3f),
                    )
                    Text(
                        text = "${item.dewPoint} °C",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(.3f),
                    )
                }
            }
            Divider()
        }
    }
}

@Composable
fun PrecipitationTable(history: List<Station>, state: LazyListState) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        itemsIndexed(history) { i, item ->
            if (i == 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.precipitation),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.road_surface),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Divider()
            }
            if (item.id == "" && i > 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                    )
                }
            } else {
                TableRow() {
                    Text(
                        text = SimpleDateFormat("HH:mm").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                    )
                    Text(
                        text = "${item.precipitation} mm",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                    )
                    Text(
                        text = "${item.roadSurface}",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Divider()
        }
    }
}

@Composable
fun WindTable(history: List<Station>, state: LazyListState) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        itemsIndexed(history) { i, item ->
            if (i == 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.wind_avg),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.wind_max),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Divider()
            }
            if (item.id == "" && i > 0) {
                TableRow {
                    Text(
                        text = SimpleDateFormat("yyyy MM dd").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                    )
                }
            } else {
                TableRow() {
                    Text(
                        text = SimpleDateFormat("HH:mm").format(item.updatedUnix),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.4f),
                    )
                    Row(
                        modifier = Modifier.weight(.3f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        WindDirection(item.windDirection, modifier = Modifier.height(15.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${item.windAverage} m/s",
                            textAlign = TextAlign.End,
                        )
                    }
                    Text(
                        text = "${item.windMax} m/s",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.3f),
                    )
                }
            }
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StationHistoryScreen(
    viewModel: StationHistoryViewModel,
    onBackClick: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val station by viewModel.station.collectAsState(emptyStation())
    val history by viewModel.history.collectAsState()

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
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        Column (
            modifier = Modifier.padding(it)
        ) {
            if (isLoading) {
                ProgressIndicator()
            } else {
                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val tabItems = listOf(
                    TabItem(stringResource(R.string.temperature)),
                    TabItem(stringResource(R.string.precipitation)),
                    TabItem(stringResource(R.string.wind)),
                )
                val pagerState = rememberPagerState {
                    tabItems.size
                }

                LaunchedEffect(selectedTabIndex) {
                    pagerState.animateScrollToPage(selectedTabIndex)
                }

                LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
                    if (!pagerState.isScrollInProgress) {
                        selectedTabIndex = pagerState.currentPage
                    }
                }
                
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                            },
                            text = {
                                Text(text = item.title)
                            },
                        )
                    }
                }

                val temperatureListState = rememberLazyListState()
                val precipitationListState = rememberLazyListState()
                val windListState = rememberLazyListState()


                LaunchedEffect(remember { derivedStateOf { temperatureListState.firstVisibleItemIndex } }, temperatureListState.isScrollInProgress) {
                    if (!temperatureListState.isScrollInProgress) {
                        precipitationListState.scrollToItem(temperatureListState.firstVisibleItemIndex, temperatureListState.firstVisibleItemScrollOffset)
                        windListState.scrollToItem(temperatureListState.firstVisibleItemIndex, temperatureListState.firstVisibleItemScrollOffset)
                    }
                }

                LaunchedEffect(remember { derivedStateOf { precipitationListState.firstVisibleItemIndex } }, precipitationListState.isScrollInProgress) {
                    if (!precipitationListState.isScrollInProgress) {
                        temperatureListState.scrollToItem(precipitationListState.firstVisibleItemIndex, precipitationListState.firstVisibleItemScrollOffset)
                        windListState.scrollToItem(precipitationListState.firstVisibleItemIndex, precipitationListState.firstVisibleItemScrollOffset)
                    }
                }

                LaunchedEffect(remember { derivedStateOf { windListState.firstVisibleItemIndex } }, windListState.isScrollInProgress) {
                    if (!windListState.isScrollInProgress) {
                        temperatureListState.scrollToItem(windListState.firstVisibleItemIndex, windListState.firstVisibleItemScrollOffset)
                        precipitationListState.scrollToItem(windListState.firstVisibleItemIndex, windListState.firstVisibleItemScrollOffset)
                    }
                }

                HorizontalPager(state = pagerState) { index ->

                    if (index == 0) {

                        TemperatureTable(history, temperatureListState)
                    }

                    if (index == 1) {

                        PrecipitationTable(history, precipitationListState)
                    }

                    if (index == 2) {
                        WindTable(history, windListState)
                    }
                }
            }
        }
    }
}
