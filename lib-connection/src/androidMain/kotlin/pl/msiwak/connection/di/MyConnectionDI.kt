package pl.msiwak.connection.di

import org.koin.dsl.koinApplication
import org.koin.dsl.module
import pl.msiwak.connection.ConnectionManager
import pl.msiwak.connection.ConnectionManagerImpl
import pl.msiwak.connection.KtorServer
import pl.msiwak.connection.KtorServerImpl
import pl.msiwak.connection.MyConnection

actual object MyConnectionDI {
    private val platformModule = module {
        single<KtorServer> { KtorServerImpl() }
        single<ConnectionManager> { ConnectionManagerImpl() }
    }
    private val koinApp = koinApplication {
        modules(module + platformModule)
    }

    actual fun getMyConnection(): MyConnection {
        return koinApp.koin.get()
    }
}
