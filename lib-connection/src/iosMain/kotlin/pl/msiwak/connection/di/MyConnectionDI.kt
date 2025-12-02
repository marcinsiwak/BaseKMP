package pl.msiwak.connection.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import pl.msiwak.connection.ConnectionManager
import pl.msiwak.connection.ElectionService
import pl.msiwak.connection.KtorClient
import pl.msiwak.connection.KtorServer
import pl.msiwak.connection.MyConnection
import pl.msiwak.connection.MyConnectionImpl
import pl.msiwak.connection.engine.EngineProvider

actual object MyConnectionDI {
    lateinit var koinApp: KoinApplication

    fun initKoin(diProvider: DIProvider) {
        koinApp = koinApplication {
            provideNetworkSwiftLibDependencyProvider(diProvider)
        }
    }

    actual fun getMyConnection(): MyConnection {
        return koinApp.koin.get()
    }

    fun KoinApplication.provideNetworkSwiftLibDependencyProvider(diProvider: DIProvider) =
        run { modules(module + networkSwiftLibDIProviderModule(diProvider)) }

    fun networkSwiftLibDIProviderModule(diProvider: DIProvider): Module = module {
        single<KtorServer> { diProvider.provideKtorServerImpl() }
        single<ConnectionManager> { diProvider.provideConnectionManager() }
    }
}
