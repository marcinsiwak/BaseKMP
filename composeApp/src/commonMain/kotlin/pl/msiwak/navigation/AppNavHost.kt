package pl.msiwak.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = Modifier.navigationBarsPadding(),
        navController = navController,
        startDestination = NavDestination.GameDestination.Graph
    ) {
        navigationGraphs.fastForEach {
            it.create(this@NavHost)
        }
    }
}
