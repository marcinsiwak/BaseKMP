package pl.msiwak.ui.aiGenerated.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.aiGenerated.AiGeneratedScreen

class AiGeneratedGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.AiGeneratedDestination.Graph>(startDestination = NavDestination.AiGeneratedDestination.Screen) {
            composable<NavDestination.AiGeneratedDestination.Screen> {
                AiGeneratedScreen()
            }
        }
    }
}
