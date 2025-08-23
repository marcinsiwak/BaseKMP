package pl.msiwak.network.engine

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

actual class EngineProvider actual constructor() {
    actual fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
        TODO("Not yet implemented")
    }
}
