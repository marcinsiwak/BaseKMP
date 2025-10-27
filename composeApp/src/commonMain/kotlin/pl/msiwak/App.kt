package pl.msiwak

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.rememberNavController
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_background
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameLightColorScheme
import pl.msiwak.cardsthegame.common.resources.MyTypography
import pl.msiwak.navigation.AppNavHost
import pl.msiwak.navigator.NavigationEvent

@Composable
@Preview
fun App(
    viewModel: MainViewModel = koinInject()
) {
    val navController = rememberNavController()
    LaunchedEffect(key1 = Unit) {
        viewModel.navigator.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToDestination -> navController.navigate(event.navDestination)
                NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }
    MaterialTheme(
        colors = GameLightColorScheme,
        typography = MyTypography()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.img_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            AppNavHost(navController)
        }
    }
}
