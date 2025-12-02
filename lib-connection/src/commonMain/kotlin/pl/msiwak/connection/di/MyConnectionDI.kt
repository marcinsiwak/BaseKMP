package pl.msiwak.connection.di

import pl.msiwak.connection.MyConnection

expect object MyConnectionDI {
    fun getMyConnection(): MyConnection
}
