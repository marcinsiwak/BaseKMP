package pl.msiwak.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.compose.koinInject
import pl.msiwak.destination.NavDestination

@Composable
fun AppNavHost(
    navController: NavHostController,
    navigationGraphs: NavigationGraphs = koinInject()
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.ScreenADestination.Graph
    ) {
        navigationGraphs.fastForEach {
            it.create(navController, this@NavHost)
        }
    }
}
