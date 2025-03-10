package pl.msiwak.network.engine

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect class EngineProvider() {
    fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig>
}
