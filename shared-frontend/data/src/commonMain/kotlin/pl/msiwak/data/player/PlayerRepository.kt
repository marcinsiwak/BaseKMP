package pl.msiwak.data.player

import kotlinx.coroutines.withContext
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.service.PlayerService

class PlayerRepository(private val playerService: PlayerService) {

    suspend fun getPlayers(): List<Player> = withContext(Dispatchers.IO) {
        return@withContext playerService.getPlayers().map {
            Player(it.number, it.name, it.surname, it.points)
        }
//        return@withContext listOf(
//            Player("1", "Jan", "Kowalski", "10"),
//            Player("2", "Jan2", "Kowalski2", "20"),
//            Player("3", "Jan3", "Kowalski3", "30"),
//            Player("4", "Jan4", "Kowalski4", "40"),
//        )
    }
}
