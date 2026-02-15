package pl.msiwak.network

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import pl.msiwak.network.engine.EngineProvider

class KtorClient(engine: EngineProvider) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val client = HttpClient(engine.getEngine()) {

    }
}