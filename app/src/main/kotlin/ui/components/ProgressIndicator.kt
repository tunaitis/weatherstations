package weatherstations.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(height: Dp = 0.dp) {
    val modifier = Modifier
        .then(if (height == 0.dp) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(height))
        .background(MaterialTheme.colorScheme.background)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ){
        CircularProgressIndicator()
    }
}
