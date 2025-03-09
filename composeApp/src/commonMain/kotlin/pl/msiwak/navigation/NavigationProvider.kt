package pl.msiwak.navigation

import pl.msiwak.ui.createtype.graph.CreateTypeGraph

data class NavigationProvider(
    val screenA: ScreenAGraph,
    val screenB: ScreenBGraph,
    val createTypeGraph: CreateTypeGraph
)
