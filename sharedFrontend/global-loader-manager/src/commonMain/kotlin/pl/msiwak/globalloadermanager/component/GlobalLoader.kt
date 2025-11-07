package pl.msiwak.globalloadermanager.component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GlobalLoader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}
