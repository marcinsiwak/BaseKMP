package pl.msiwak.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import pl.msiwak.network.KtorClient
import pl.msiwak.shared.model.player.ApiPlayer

class PlayerApi(private val ktorClient: KtorClient) {

    suspend fun getPlayers(): List<ApiPlayer> {
        val response: List<ApiPlayer> = ktorClient.httpClient.get("api/players").body()
        return response
    }
}
