package pl.msiwak.ui.game.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.animation.customEnterTransition
import pl.msiwak.animation.customExitTransition
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.game.LobbyScreen
import pl.msiwak.ui.game.finish.FinishScreen
import pl.msiwak.ui.game.gameplay.CardsPreparationScreen
import pl.msiwak.ui.game.round.RoundScreen
import pl.msiwak.ui.game.roundinfo.RoundInfoScreen
import pl.msiwak.ui.game.start.StartScreen

class GameGraph : NavigationGraph {

    override fun create(
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.GameDestination.Graph>(startDestination = NavDestination.GameDestination.StartScreen) {
            composable<NavDestination.GameDestination.StartScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                StartScreen()
            }
            composable<NavDestination.GameDestination.LobbyScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                LobbyScreen()
            }
            composable<NavDestination.GameDestination.CardsPreparationScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                CardsPreparationScreen()
            }
            composable<NavDestination.GameDestination.RoundInfoScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                RoundInfoScreen()
            }
            composable<NavDestination.GameDestination.RoundScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                RoundScreen()
            }
            composable<NavDestination.GameDestination.FinishScreen>(
                enterTransition = customEnterTransition,
                exitTransition = customExitTransition
            ) {
                FinishScreen()
            }
        }
    }
}
