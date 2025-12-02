package pl.msiwak.connection.di

import org.koin.dsl.module
import pl.msiwak.connection.ElectionService
import pl.msiwak.connection.KtorClient
import pl.msiwak.connection.MyConnection
import pl.msiwak.connection.MyConnectionImpl
import pl.msiwak.connection.engine.EngineProvider

val module = module {
    single { ElectionService(get()) }
    single { KtorClient(get()) }
    single { EngineProvider() }
    single<MyConnection> { MyConnectionImpl(get(), get(), get(), get()) }
}