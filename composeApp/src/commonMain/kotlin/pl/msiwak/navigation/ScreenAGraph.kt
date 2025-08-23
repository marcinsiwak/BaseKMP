package pl.msiwak.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import pl.msiwak.Greeting
import pl.msiwak.destination.NavDestination
import pl.msiwak.graph.NavigationGraph

// Example - remove during development, ui should be in separate feature module

class ScreenAGraph : NavigationGraph {

    override fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NavDestination.ScreenADestination.Graph>(startDestination = NavDestination.ScreenADestination.Screen) {
            composable<NavDestination.ScreenADestination.Screen> {
                Column {
                    var showContent by remember { mutableStateOf(false) }
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { showContent = !showContent }) {
                            Text("Click me!")
                        }
                        Button(onClick = {
                            navController.navigate(NavDestination.ExampleDestination.Screen)
                        }) {
                            Text("Navigate to Screen B")
                        }
                        Button(onClick = {
                            navController.navigate(NavDestination.AiGeneratedDestination.Screen)
                        }) {
                            Text("Navigate to AI Generated Screen")
                        }
                        AnimatedVisibility(showContent) {
                            val greeting = remember { Greeting().greet() }
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(painterResource(Res.drawable.compose_multiplatform), null)
                                Text("Compose: $greeting")
                            }
                        }
                    }
                }
            }
        }
    }
}
