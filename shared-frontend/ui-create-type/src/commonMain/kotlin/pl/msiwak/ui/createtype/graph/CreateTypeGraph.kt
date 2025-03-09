package pl.msiwak.ui.createtype.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.createtype.CreateTypeScreen

class CreateTypeGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.CreateTypeDestination.Graph>(startDestination = NavDestination.CreateTypeDestination.Screen) {
            composable<NavDestination.CreateTypeDestination.Screen> {
                CreateTypeScreen()
            }
        }
    }
}
