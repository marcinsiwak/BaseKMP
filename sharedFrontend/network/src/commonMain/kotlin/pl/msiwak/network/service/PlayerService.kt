package pl.msiwak.network.service

import pl.msiwak.network.api.PlayerApi
import pl.msiwak.shared.model.player.ApiPlayer

class PlayerService(private val playerApi: PlayerApi) {
    suspend fun getPlayers() = playerApi.getPlayers()

    suspend fun connectPlayer(player: ApiPlayer) = playerApi.connectPlayer(player)

    suspend fun disconnectPlayer(playerId: String) = playerApi.disconnectPlayer(playerId)

    suspend fun getConnectedPlayers() = playerApi.getConnectedPlayers()
}
