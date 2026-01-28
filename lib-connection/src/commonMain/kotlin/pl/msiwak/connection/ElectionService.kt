@file:OptIn(ExperimentalTime::class)
package pl.msiwak.connection

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ElectionService(
    private val connectionManager: ConnectionManager,
    // private val globalLoaderManager: GlobalLoaderManager
) {
    private var electionInProgress = false
    private val candidates = hashMapOf<String, DeviceCandidate>()
    private val candidatesMutex = Mutex()

    private val _hostIp = MutableSharedFlow<String>()
    val hostIp: SharedFlow<String> = _hostIp.asSharedFlow()

    private var currentHasSession = false
    private var currentLastUpdate: Long? = null

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        println("ElectionService Error: ${throwable.message}")
    }

    private var electionJob: Job? = null

    fun startElection() {
        if (electionJob?.isActive == true) return

        electionJob = CoroutineScope(Dispatchers.IO).launch {
            launch(errorHandler + coroutineContext) {
                connectionManager.startUdpListener(port = 60000).collectLatest { message ->
                    handleElectionMessage(message)

                    val host = candidatesMutex.withLock {
                        candidates.values.find { it.isHost }?.ipAddress
                    }

                    host?.let {
                        println("Host is: $it")
                        _hostIp.emit(it)
                    }
                }
            }

            launch(errorHandler) {
                while (isActive) {
                    sendMessage()
                    delay(1000)
                }
            }

            launch(errorHandler) {
                while (isActive) {
                    delay(3000)

                    val shouldElect = candidatesMutex.withLock {
                        !electionInProgress && candidates.isNotEmpty() && candidates.values.none { it.isHost }
                    }

                    if (shouldElect) {
                        conductElection()
                    }

                    cleanupOldCandidates()
                }
            }
        }
    }

    private suspend fun handleElectionMessage(message: String) {
        val electionMessage = Json.decodeFromString<ElectionMessage>(message)

        candidatesMutex.withLock {
            with(electionMessage) {
                // Only trust direct messages from the sender
                candidates[senderIp] = DeviceCandidate(
                    ipAddress = senderIp,
                    networkNumber = senderIp.substringAfterLast(".").toInt(),
                    isHost = hostIp == senderIp, // Only if sender claims to be host
                    lastSeen = Clock.System.now().toEpochMilliseconds(),
                    hasSession = hasGameSession,
                    lastSessionUpdate = lastSessionUpdate
                )
            }
        }
    }

    private suspend fun conductElection() {
        candidatesMutex.withLock {
            electionInProgress = true

            // Deterministic election with IP-based tiebreaker
            val winner = candidates.values
                .filter { it.hasSession && it.lastSessionUpdate != null }
                .maxWithOrNull(compareBy<DeviceCandidate> { it.lastSessionUpdate ?: 0 }
                    .thenBy { it.ipAddress })
                ?: candidates.values
                    .maxWithOrNull(compareBy<DeviceCandidate> { it.networkNumber }
                        .thenBy { it.ipAddress })
                ?: run {
                    electionInProgress = false
                    return
                }

            candidates[winner.ipAddress] = winner.copy(isHost = true)
            electionInProgress = false
        }
    }

    private suspend fun cleanupOldCandidates() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val timeout = 4000

        candidatesMutex.withLock {
            val toRemove = candidates.filter { (_, candidate) ->
                currentTime - candidate.lastSeen > timeout
            }.keys

            toRemove.forEach { candidates.remove(it) }
        }
    }

    private suspend fun sendMessage() {
        val ip = connectionManager.getLocalIpAddress() ?: return

        val hostIp = candidatesMutex.withLock {
            candidates.values.find { it.isHost }?.ipAddress
        }

        val message = Json.encodeToString(
            ElectionMessage.serializer(),
            ElectionMessage(
                senderIp = ip,
                hostIp = hostIp,
                hasGameSession = currentHasSession,
                lastSessionUpdate = currentLastUpdate
            )
        )

        connectionManager.broadcastMessage(port = 60000, msg = message)
    }

    fun setHasSession(hasGameSession: Boolean, lastUpdate: Long) {
        currentHasSession = hasGameSession
        currentLastUpdate = lastUpdate
    }

    suspend fun clearHost() {
        candidatesMutex.withLock {
            candidates.forEach { (key, candidate) ->
                candidates[key] = candidate.copy(isHost = false)
            }
        }
        _hostIp.emit("")
    }
}

data class DeviceCandidate(
    val ipAddress: String,
    val networkNumber: Int,
    val isHost: Boolean,
    val lastSeen: Long,
    val hasSession: Boolean,
    val lastSessionUpdate: Long? = null
)

@Serializable
data class ElectionMessage(
    val senderIp: String,
    val hostIp: String? = null,
    val hasGameSession: Boolean,
    val lastSessionUpdate: Long? = null
)