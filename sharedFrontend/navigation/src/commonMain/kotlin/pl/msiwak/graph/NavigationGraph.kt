package pl.msiwak.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavigationGraph {
    fun create(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    )
}
