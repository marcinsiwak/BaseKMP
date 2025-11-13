package pl.msiwak.cardsthegame.remoteconfig

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val MIN_PLAYERS_KEY = "min_players_number"

interface RemoteConfig {
    suspend fun fetch()
    fun getMinPlayers(): Long
}

class RemoteConfigImpl : RemoteConfig {
    private val remoteConfig = Firebase.remoteConfig

    override suspend fun fetch() {
        remoteConfig.settings {
            minimumFetchInterval = Duration.ZERO
            fetchTimeout = 1.minutes
        }
        remoteConfig.fetchAndActivate()
    }

    override fun getMinPlayers(): Long = remoteConfig.get<Long>(MIN_PLAYERS_KEY)

}
