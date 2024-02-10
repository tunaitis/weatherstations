package weatherstations.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import weatherstations.data.LocationDataSource
import weatherstations.data.SettingsStore
import weatherstations.data.StationsRepository
import weatherstations.ui.about.AboutScreen
import weatherstations.ui.home.HomeScreen
import weatherstations.ui.home.HomeViewModel
import weatherstations.ui.search.SearchScreen
import weatherstations.ui.search.SearchViewModel
import weatherstations.ui.settings.SettingsScreen
import weatherstations.ui.settings.SettingsViewModel
import weatherstations.ui.stationcam.StationCamScreen
import weatherstations.ui.stationcam.StationCamViewModel
import weatherstations.ui.stationhistory.StationHistoryScreen
import weatherstations.ui.stationhistory.StationHistoryViewModel
import weatherstations.ui.theme.WeatherStationsTheme

@Composable
fun App(
    appViewModel: AppViewModel,
    settingsStore: SettingsStore,
    stationsRepository: StationsRepository,
    locationDataSource: LocationDataSource,
) {
    val isDark = appViewModel.isDarkTheme.collectAsState().value

    WeatherStationsTheme(
        darkTheme = isDark
    ) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "home",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable("home") {
                val viewModel = viewModel {
                    HomeViewModel(settingsStore, stationsRepository, locationDataSource)
                }

                HomeScreen(
                    viewModel,
                    onSearchClick = {
                        navController.navigate("search")
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onAboutClick = {
                        navController.navigate("about")
                    },
                    onCameraClick = {
                        navController.navigate("camera/$it")
                    },
                    onHistoryClick = {
                        navController.navigate("history/$it")
                    },
                )
            }
            composable("camera/{id}") {backStackEntry ->
                val id =  backStackEntry.arguments?.getString("id") ?: ""
                val viewModel = viewModel {
                    StationCamViewModel(stationsRepository, id)
                }
                StationCamScreen(
                    viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("history/{id}") {backStackEntry ->
                val id =  backStackEntry.arguments?.getString("id") ?: ""
                val viewModel = viewModel {
                    StationHistoryViewModel(stationsRepository, id)
                }
                StationHistoryScreen(
                    viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("search") {
                val viewModel = viewModel {
                    SearchViewModel(stationsRepository)
                }
                SearchScreen(
                    viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("settings") {
                val theme by appViewModel.theme.collectAsState()
                val viewModel = viewModel {
                    SettingsViewModel(settingsStore)
                }
                SettingsScreen(
                    viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    theme = theme,
                    onChangeTheme = appViewModel::setTheme
                )
            }
            composable("about") {
                AboutScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
