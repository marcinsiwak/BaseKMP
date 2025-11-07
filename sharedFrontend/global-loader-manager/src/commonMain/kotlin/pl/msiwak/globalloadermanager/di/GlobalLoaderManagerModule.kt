package pl.msiwak.globalloadermanager.di

import org.koin.dsl.module
import pl.msiwak.globalloadermanager.GlobalLoaderManager

val globalLoaderManagerModule = module {
    single { GlobalLoaderManager() }
}