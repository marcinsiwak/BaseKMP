package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.navigation.NavigationGraphs

internal val navigationModule = module {
    single { NavigationGraphs() }
}
