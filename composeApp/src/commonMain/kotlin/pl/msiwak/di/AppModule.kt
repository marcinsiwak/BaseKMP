package pl.msiwak.di

import pl.msiwak.basekmp.remoteconfig.di.remoteConfigModule
import pl.msiwak.globalloadermanager.di.globalLoaderManagerModule

internal val appModule = listOf(
    navigationModule,
    viewModelModule,
    useCaseModule,
    repositoryModule,
    networkModule,
    globalLoaderManagerModule,
    remoteConfigModule
)
