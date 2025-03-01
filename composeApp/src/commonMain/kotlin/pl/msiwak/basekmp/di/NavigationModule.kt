package pl.msiwak.basekmp.di

import org.koin.dsl.module
import pl.msiwak.basekmp.navigation.NavigationProvider
import pl.msiwak.basekmp.navigation.ScreenAGraph
import pl.msiwak.basekmp.navigation.ScreenBGraph

internal val navigationModule = module {
    single { ScreenAGraph() } // remove during development
    single { ScreenBGraph() } // remove during development
    single { NavigationProvider(get(), get()) }
}