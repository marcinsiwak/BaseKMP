package pl.msiwak.connection.engine

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect class EngineProvider() {
    fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig>
}
