package pl.msiwak.navigation

import pl.msiwak.ui.createprediction.graph.CreateTypeGraph

data class NavigationProvider(
    val screenA: ScreenAGraph,
    val screenB: ScreenBGraph,
    val createTypeGraph: CreateTypeGraph
)
