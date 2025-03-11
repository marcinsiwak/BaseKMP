package pl.msiwak.ui.createprediction.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.createprediction.CreatePredictionScreen

class CreatePredictionGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.CreatePredictionDestination.Graph>(startDestination = NavDestination.CreatePredictionDestination.Screen) {
            composable<NavDestination.CreatePredictionDestination.Screen> {
                CreatePredictionScreen()
            }
        }
    }
}
