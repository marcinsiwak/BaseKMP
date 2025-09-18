package pl.msiwak

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
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
    MaterialTheme {
        AppNavHost(navController)
    }
}
