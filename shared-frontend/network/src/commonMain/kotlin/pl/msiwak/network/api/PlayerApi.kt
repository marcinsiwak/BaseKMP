package pl.msiwak.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.msiwak.network.KtorClient
import pl.msiwak.shared.model.player.ApiPlayer
import pl.msiwak.shared.model.player.ApiPlayersResponse

class PlayerApi(private val ktorClient: KtorClient) {

    suspend fun getPlayers(): List<ApiPlayer> {
        val response: ApiPlayersResponse = ktorClient.client.get("/players").body()
        return response.players
    }

    suspend fun connectPlayer(player: ApiPlayer) {
        return ktorClient.connect(player.name)
    }

    suspend fun disconnectPlayer(playerId: String): Boolean {
        return ktorClient.client.delete("/players/$playerId/disconnect").body()
    }

    suspend fun getConnectedPlayers(): List<ApiPlayer> {
        val response: ApiPlayersResponse = ktorClient.client.get("/players/connected").body()
        return response.players
    }
}
