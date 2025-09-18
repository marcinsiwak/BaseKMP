package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.navigation.NavigationGraphs
import pl.msiwak.navigator.Navigator

internal val navigationModule = module {
    single { NavigationGraphs() }
    single { Navigator() }
}
