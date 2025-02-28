package pl.msiwak.basekmp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.msiwak.basekmp.destination.NavDestination
import pl.msiwak.basekmp.navigation.NavigationProvider
import pl.msiwak.basekmp.navigation.ScreenAGraph
import pl.msiwak.basekmp.navigation.ScreenBGraph

private val navigationProvider = NavigationProvider(ScreenAGraph(), ScreenBGraph())

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
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
}