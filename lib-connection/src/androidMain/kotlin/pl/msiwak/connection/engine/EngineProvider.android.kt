package pl.msiwak.connection.engine

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual class EngineProvider actual constructor() {
    actual fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = CIO
}