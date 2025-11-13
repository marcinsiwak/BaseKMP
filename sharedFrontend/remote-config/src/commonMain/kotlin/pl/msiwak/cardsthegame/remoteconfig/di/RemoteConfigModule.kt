package pl.msiwak.cardsthegame.remoteconfig.di

import org.koin.dsl.module
import pl.msiwak.cardsthegame.remoteconfig.RemoteConfigImpl
import pl.msiwak.cardsthegame.remoteconfig.RemoteConfig

/**
 * Koin DI module for Remote Config.
 */
val remoteConfigModule = module {
    single<RemoteConfig> { RemoteConfigImpl() }
}