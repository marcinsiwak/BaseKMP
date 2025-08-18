package pl.msiwak.network.service

import pl.msiwak.network.api.PlayerApi

class PlayerService(private val playerApi: PlayerApi) {
    suspend fun getPlayers() = playerApi.getPlayers()
}
