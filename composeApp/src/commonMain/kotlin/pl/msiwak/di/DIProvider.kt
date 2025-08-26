package pl.msiwak.di

import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorServer

interface DIProvider {
    fun provideKtorServerImpl(): KtorServer
    fun provideConnectionManager(): ConnectionManager
}