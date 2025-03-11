package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.navigation.NavigationProvider
import pl.msiwak.navigation.ScreenAGraph
import pl.msiwak.navigation.ScreenBGraph
import pl.msiwak.ui.createprediction.graph.CreatePredictionGraph

internal val navigationModule = module {
    single { ScreenAGraph() } // remove during development
    single { ScreenBGraph() } // remove during development
    single { NavigationProvider(get(), get(), get()) }
    single { CreatePredictionGraph() }
}
