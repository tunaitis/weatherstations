package weatherstations.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import weatherstations.R
import weatherstations.ui.home.HomeScreenDestination

data class ToggleButtonItem(
    val title: Int,
    val value: String,
)

@Composable
fun ToggleButton(
    selected: String,
    options: List<ToggleButtonItem>,
    onChange: (String) -> Unit
) {
    Card (
        modifier = Modifier
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEachIndexed { index, item ->
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp, 15.dp)
                        .clickable { onChange(item.value) }
                ) {
                    val isActive = selected == item.value
                    val color = if (isActive) MaterialTheme.colorScheme.primary else Color.Unspecified
                    Text(
                        text = stringResource(item.title),
                        color = color,
                    )
                    RadioButton(
                        selected = isActive,
                        onClick = { onChange(item.value) }
                    )
                }

                if (index < options.size) {
                    Divider(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBackClick: () -> Unit, theme: String, onChangeTheme: (String) -> Unit) {
    val homeRoute by viewModel.homeRoute.collectAsState()
    val locale by viewModel.locale.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
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
        Column(
            modifier = Modifier
                .padding(it)
                .then(Modifier.padding(15.dp, 0.dp))
        ) {

            Text(
                text= stringResource(R.string.home_screen),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ToggleButton(
                selected = homeRoute,
                options = listOf(
                    ToggleButtonItem(HomeScreenDestination.Stations.title, HomeScreenDestination.Stations.route),
                    ToggleButtonItem(HomeScreenDestination.Starred.title, HomeScreenDestination.Starred.route),
                    ToggleButtonItem(HomeScreenDestination.Map.title, HomeScreenDestination.Map.route),
                ),
                onChange = viewModel::setHomeScreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text= stringResource(R.string.language),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ToggleButton(
                selected = locale,
                options = listOf(
                    ToggleButtonItem(R.string.defaultValue, ""),
                    ToggleButtonItem(R.string.lithuanian, "lt-LT"),
                    ToggleButtonItem(R.string.english, "en-US"),
                ),
                onChange = viewModel::setLocale
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text= stringResource(R.string.theme),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ToggleButton(
                selected = theme,
                options = listOf(
                    ToggleButtonItem(R.string.defaultValue, ""),
                    ToggleButtonItem(R.string.dark, "dark"),
                    ToggleButtonItem(R.string.light, "light"),
                ),
                onChange = onChangeTheme
            )
        }
    }
}