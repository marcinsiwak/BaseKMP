package pl.msiwak.basekmp.remoteconfig.di

import org.koin.dsl.module
import pl.msiwak.basekmp.remoteconfig.RemoteConfigImpl
import pl.msiwak.basekmp.remoteconfig.RemoteConfig

/**
 * Koin DI module for Remote Config.
 */
val remoteConfigModule = module {
    single<RemoteConfig> { RemoteConfigImpl() }
}