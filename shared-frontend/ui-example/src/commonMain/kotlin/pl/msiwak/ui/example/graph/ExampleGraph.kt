package pl.msiwak.ui.example.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.example.ExampleScreen

class ExampleGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.ExampleDestination.Graph>(startDestination = NavDestination.ExampleDestination.Screen) {
            composable<NavDestination.ExampleDestination.Screen> {
                ExampleScreen()
            }
        }
    }
}
