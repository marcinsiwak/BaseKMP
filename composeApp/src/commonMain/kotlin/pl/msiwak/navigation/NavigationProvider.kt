package pl.msiwak.navigation

import pl.msiwak.ui.example.graph.ExampleGraph

data class NavigationProvider(
    val screenA: ScreenAGraph,
    val screenB: ScreenBGraph,
    val exampleGraph: ExampleGraph
)
