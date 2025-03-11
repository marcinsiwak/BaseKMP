package pl.msiwak.navigation

import pl.msiwak.ui.createprediction.graph.CreatePredictionGraph

data class NavigationProvider(
    val screenA: ScreenAGraph,
    val screenB: ScreenBGraph,
    val createPredictionGraph: CreatePredictionGraph
)
