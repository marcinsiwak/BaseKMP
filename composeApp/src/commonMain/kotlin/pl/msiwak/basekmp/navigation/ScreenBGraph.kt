package pl.msiwak.basekmp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import pl.msiwak.basekmp.destination.NavDestination
import pl.msiwak.basekmp.graph.NavigationGraph

// Example - remove during development, ui should be in separate feature module
class ScreenBGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.ScreenBDestination.Graph>(startDestination = NavDestination.ScreenBDestination.Screen()) {
            composable<NavDestination.ScreenBDestination.Screen> { backStackEntry ->
                val arguments = backStackEntry.toRoute<NavDestination.ScreenBDestination.Screen>()
                Column {
                    Text("Screen B, Name: ${arguments.name}")
                }
            }
        }
    }
}
