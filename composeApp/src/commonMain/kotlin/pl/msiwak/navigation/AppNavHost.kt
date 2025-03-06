package pl.msiwak.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.compose.koinInject
import pl.msiwak.destination.NavDestination

@Composable
fun AppNavHost(
    navController: NavHostController,
    navigationProvider: NavigationProvider = koinInject()
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.ScreenADestination.Graph
    ) {
        with(navigationProvider) {
            screenA.create(navController, this@NavHost)
            screenB.create(navController, this@NavHost)
        }
    }
}
