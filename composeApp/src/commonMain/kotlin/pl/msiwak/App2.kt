package pl.msiwak

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameLightColorScheme
import pl.msiwak.cardsthegame.common.resources.MyTypography

@Composable
@Preview
fun App2(
    viewModel: MainViewModel2 = koinInject()
) {

    MaterialTheme(
        colors = GameLightColorScheme,
        typography = MyTypography()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Hello World!")
        }
    }
}
