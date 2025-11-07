package pl.msiwak.di

import pl.msiwak.globalloadermanager.di.globalLoaderManagerModule

internal val appModule = listOf(
    navigationModule,
    viewModelModule,
    useCaseModule,
    repositoryModule,
    networkModule,
    globalLoaderManagerModule
)
