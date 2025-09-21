package pl.msiwak.graph

import androidx.navigation.NavGraphBuilder

interface NavigationGraph {
    fun create(
        navGraphBuilder: NavGraphBuilder
    )
}
