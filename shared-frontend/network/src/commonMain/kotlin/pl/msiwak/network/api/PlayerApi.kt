package pl.msiwak.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import pl.msiwak.network.KtorClient
import pl.msiwak.shared.model.player.ApiPlayer
import pl.msiwak.shared.model.player.ApiPlayersResponse

class PlayerApi(private val ktorClient: KtorClient) {

    suspend fun getPlayers(): List<ApiPlayer> {
        val response: ApiPlayersResponse = ktorClient.httpClient.get("/api/players").body()
        return response.players
    }
}
