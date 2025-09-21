package pl.msiwak.ui.game.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.game.GameScreen
import pl.msiwak.ui.game.start.StartScreen

class GameGraph : NavigationGraph {

    override fun create(
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.GameDestination.Graph>(startDestination = NavDestination.GameDestination.StartScreen) {
            composable<NavDestination.GameDestination.StartScreen> {
                StartScreen()
            }
            composable<NavDestination.GameDestination.GameScreen> {
                GameScreen()
            }
        }
    }
}
