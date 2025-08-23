package pl.msiwak.ui.game.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.game.GameScreen

class GameGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.GameDestination.Graph>(startDestination = NavDestination.GameDestination.Screen) {
            composable<NavDestination.GameDestination.Screen> {
                GameScreen()
            }
        }
    }
}
