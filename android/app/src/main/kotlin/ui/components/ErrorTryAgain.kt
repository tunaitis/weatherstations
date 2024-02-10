package weatherstations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import weatherstations.R
import weatherstations.data.ErrorType

@Composable
fun ErrorTryAgain(error: ErrorType, onTryAgain: () -> Unit) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.error),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(text = error.message)
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = onTryAgain) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    }
}