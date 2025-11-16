package pl.msiwak.cardsthegame.remoteconfig

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val MIN_PLAYERS_KEY = "min_players_number"
private const val ROUND_TIME_KEY = "round_default_time"

interface RemoteConfig {
    suspend fun fetch()
    fun getMinPlayers(): Long
    fun getRoundDefaultTime(): Int
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

    override fun getRoundDefaultTime(): Int = remoteConfig.get<Long>(ROUND_TIME_KEY).toInt()

}
