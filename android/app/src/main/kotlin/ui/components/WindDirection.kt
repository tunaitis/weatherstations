package weatherstations.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.South
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.West
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WindDirection(name: String?, modifier: Modifier = Modifier) {
    when (name?.toLowerCase()) {
        "šiaurės" -> Icon(Icons.Default.South, contentDescription = null, modifier = modifier)
        "šiaurės rytų" -> Icon(Icons.Default.SouthWest, contentDescription = null, modifier = modifier)
        "rytų" -> Icon(Icons.Default.West, contentDescription = null, modifier = modifier)
        "pietryčių" -> Icon(Icons.Default.NorthWest, contentDescription = null, modifier = modifier)
        "pietų" -> Icon(Icons.Default.North, contentDescription = null, modifier = modifier)
        "pietvakarių" -> Icon(Icons.Default.NorthEast, contentDescription = null, modifier = modifier)
        "vakarų" -> Icon(Icons.Default.East, contentDescription = null, modifier = modifier)
        "šiaurės vakarų" -> Icon(Icons.Default.SouthEast, contentDescription = null, modifier = modifier)
    }
}