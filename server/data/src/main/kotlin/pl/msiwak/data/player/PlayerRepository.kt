package pl.msiwak.data.player

import pl.msiwak.network.player.PlayersService
import pl.msiwak.shared.model.player.ApiPlayer


class PlayerRepository(private val playerService: PlayersService) {

    fun getPlayers(): List<ApiPlayer> {
        return listOf(
            ApiPlayer("id1", "name1", "surname1", "points1", "number1"),
            ApiPlayer("id2", "name2", "surname2", "points2", "number2"),
            ApiPlayer("id3", "name3", "surname3", "points3", "number3"),
            ApiPlayer("id4", "name4", "surname4", "points4", "number4"),
            ApiPlayer("id5", "name5", "surname5", "points5", "number5")
        )
//        return playerService.getPlayers()
    }
}
