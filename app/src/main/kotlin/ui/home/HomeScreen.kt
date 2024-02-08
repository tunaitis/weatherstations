package weatherstations.ui.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import weatherstations.R
import weatherstations.data.ErrorType
import weatherstations.ui.components.ErrorTryAgain
import weatherstations.ui.components.ProgressIndicator
import weatherstations.ui.components.StationCard

enum class HomeScreenTab {
    Stations, Starred, Map
}

sealed class HomeScreenDestination(
    val route: String,
    val title: Int,
    val icon: ImageVector,
    val activeIcon: ImageVector,
) {
    data object Stations : HomeScreenDestination("stations", R.string.stations, Icons.Outlined.Home, Icons.Filled.Home)
    data object Starred : HomeScreenDestination("starred", R.string.starred, Icons.Outlined.StarBorder, Icons.Filled.Star)
    data object Map : HomeScreenDestination("map", R.string.map, Icons.Outlined.Place, Icons.Filled.Place)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onCameraClick: (String) -> Unit,
    onHistoryClick: (String) -> Unit,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (isLoading) {
        ProgressIndicator()
    } else if (error != null) {
        ErrorTryAgain(
            error = error ?: ErrorType.UnknownError,
            onTryAgain = viewModel::refreshStations,
        )
    } else  {
        HomeScreenContent(
            viewModel,
            onSearchClick,
            onSettingsClick,
            onAboutClick,
            onCameraClick,
            onHistoryClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onCameraClick: (String) -> Unit,
    onHistoryClick: (String) -> Unit,
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val stations by viewModel.stations.collectAsState()
    val selectedStation = viewModel.selectedStation.collectAsState()
    val isDropdownMenuExpanded by viewModel.isDropdownMenuExpanded.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                title = {
                    Text(text = stringResource(id = R.string.weather_stations))
                },
                scrollBehavior = if (currentDestination?.hierarchy?.any { it.route == "map" } == true) null else scrollBehavior,
                actions = {
                    IconButton( onClick = onSearchClick, ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                    IconButton( onClick = viewModel::expandDropdown, ) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = isDropdownMenuExpanded,
                        onDismissRequest = viewModel::collapseDropdownMenu
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.settings)) },
                            onClick = {
                                onSettingsClick()
                                viewModel.collapseDropdownMenu()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.about)) },
                            onClick = {
                                onAboutClick()
                                viewModel.collapseDropdownMenu()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val bottomNavItems = listOf(
                    HomeScreenDestination.Stations,
                    HomeScreenDestination.Starred,
                    HomeScreenDestination.Map,
                )
                bottomNavItems.forEach {screen ->
                    val isActive = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem (
                        selected = isActive,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(stringResource(screen.title)) },
                        icon = { Icon(if (isActive) screen.activeIcon else screen.icon, contentDescription = null) },
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = bottomNavController,
            startDestination = viewModel.homeRoute,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(it),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable(HomeScreenDestination.Stations.route) {
                StationListScreen(
                    stations,
                    onStarClick = viewModel::toggleStar,
                    onCameraClick = onCameraClick,
                    onHistoryClick = onHistoryClick,
                )
            }
            composable(HomeScreenDestination.Starred.route) {
                val starredStations = stations.filter { station -> station.isStarred }
                StationListScreen(
                    starredStations,
                    onStarClick = viewModel::toggleStar,
                    onCameraClick = onCameraClick,
                    onHistoryClick = onHistoryClick,
                )
            }
            composable(HomeScreenDestination.Map.route) {
                StationMapScreen(stations, onMarkerClick = viewModel::selectStation)
            }

        }

        if (selectedStation.value != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.deselectStation()
                },
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    StationCard(
                        station = selectedStation.value !!,
                        onStarClick = viewModel::toggleStar,
                        onCameraClick = onCameraClick,
                        onHistoryClick = onHistoryClick,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = {
                            viewModel.deselectStation()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}
