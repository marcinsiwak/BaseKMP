package pl.msiwak

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import pl.msiwak.basekmp.common.resources.LightColorScheme
import pl.msiwak.globalloadermanager.component.GlobalLoader
import pl.msiwak.navigation.AppNavHost
import pl.msiwak.navigator.NavigationEvent

@Composable
@Preview
fun App(
    viewModel: MainViewModel = koinInject()
) {
    val viewState = viewModel.viewState.collectAsState()

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
        colors = LightColorScheme
    ) {
        Box(
            modifier = Modifier.fillMaxSize().imePadding()
        ) {
            AppNavHost(navController)

            if (viewState.value.isLoading) {
                GlobalLoader(
                    modifier = Modifier.align(Center)
                )
            }
        }
    }
}
