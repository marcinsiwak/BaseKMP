package pl.msiwak.globalloadermanager.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp

@Composable
fun GlobalLoader(modifier: Modifier = Modifier, message: String? = null) {
    Column(
        modifier = modifier.background(Color(0xB2CCCCCC)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        message?.let { Text(text = it, color = Black) }
    }
}
