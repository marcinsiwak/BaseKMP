package pl.msiwak.connection.di

import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import pl.msiwak.connection.ConnectionManager
import pl.msiwak.connection.ConnectionManagerImpl
import pl.msiwak.connection.KtorServer
import pl.msiwak.connection.KtorServerImpl
import pl.msiwak.connection.MyConnection

actual object MyConnectionDI {
    lateinit var koinApp: KoinApplication

    val iosModule = module {
        single<KtorServer> { KtorServerImpl() }
        single<ConnectionManager> { ConnectionManagerImpl() }
    }

    fun initKoin() {
        koinApp = koinApplication {
            modules(module + iosModule)
        }
    }

    actual fun getMyConnection(): MyConnection {
        return koinApp.koin.get()
    }
}
