package pl.msiwak.connection.di

import pl.msiwak.connection.ConnectionManager
import pl.msiwak.connection.KtorServer

interface DIProvider {
    fun provideKtorServerImpl(): KtorServer
    fun provideConnectionManager(): ConnectionManager
}