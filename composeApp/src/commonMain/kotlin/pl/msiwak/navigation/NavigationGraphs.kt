package pl.msiwak.navigation

import pl.msiwak.graph.NavigationGraph
import pl.msiwak.ui.aiGenerated.graph.AiGeneratedGraph
import pl.msiwak.ui.example.graph.ExampleGraph

class NavigationGraphs : List<NavigationGraph> by listOf(
    ScreenAGraph(),
    ScreenBGraph(),
    ExampleGraph(),
    AiGeneratedGraph()
)
