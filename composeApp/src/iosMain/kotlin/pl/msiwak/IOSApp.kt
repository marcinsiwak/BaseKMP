package pl.msiwak

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.msiwak.di.appModule
import pl.msiwak.network.KtorServer
import pl.msiwak.di.DIProvider

fun initKoin(onKoinStart: KoinApplication.() -> Unit) {
    startKoin {
        onKoinStart()
        modules(appModule)
    }
}

fun KoinApplication.provideNetworkSwiftLibDependencyProvider(diProvider: DIProvider) =
    run { modules(networkSwiftLibDIProviderModule(diProvider)) }

fun networkSwiftLibDIProviderModule(diProvider: DIProvider): Module = module {
    single<KtorServer> { diProvider.provideKtorServerImpl() }
}