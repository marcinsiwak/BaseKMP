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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ElectionService(
    private val connectionManager: ConnectionManager,
//    private val globalLoaderManager: GlobalLoaderManager
) {
    private var electionInProgress = false
    private val candidates = hashMapOf<String, DeviceCandidate>()

    private val _hostIp = MutableSharedFlow<String>()
    val hostIp: SharedFlow<String> = _hostIp.asSharedFlow()

    private var currentHasGameSession = false

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
                    val host = candidates.values.find { it.isHost }?.ipAddress
                    host?.let { _hostIp.emit(it) }
//                        ?: globalLoaderManager.showLoading(GlobalLoaderMessageType.MISSING_HOST)
//                    println("Candidates: ${candidates.mapValues { it.value }}")
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
                    if (!electionInProgress && candidates.isNotEmpty() && candidates.values.none { it.isHost }) {
                        conductElection()
                    }
                    cleanupOldCandidates()
                }
            }
        }
    }

    private fun handleElectionMessage(message: String) {
        val electionMessage = Json.decodeFromString<ElectionMessage>(message)

        with(electionMessage) {
            candidates[senderIp] = DeviceCandidate(
                ipAddress = senderIp,
                networkNumber = senderIp.substringAfterLast(".").toInt(),
                isHost = hostIp == senderIp,
                lastSeen = Clock.System.now().toEpochMilliseconds(),
                hasGameSession = hasGameSession
            )
            if (hostIp != null && hostIp != senderIp) {
                candidates[hostIp] = DeviceCandidate(
                    ipAddress = hostIp,
                    networkNumber = hostIp.substringAfterLast(".").toInt(),
                    isHost = true,
                    lastSeen = candidates[hostIp]?.lastSeen ?: 0,
                    hasGameSession = hasGameSession
                )
            }
        }
    }

    private fun conductElection() {
        electionInProgress = true

        val winner = candidates.values.filter { it.hasGameSession }.maxByOrNull { it.networkNumber }
            ?: candidates.values.maxByOrNull { it.networkNumber }
            ?: run {
                electionInProgress = false
                return
            }

        candidates[winner.ipAddress] = winner.copy(isHost = true)
        electionInProgress = false
    }

    private fun cleanupOldCandidates() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val timeout = 3000

        candidates.entries.removeAll { (_, candidate) ->
            currentTime - candidate.lastSeen > timeout
        }
    }

    private suspend fun sendMessage() {
        val ip = connectionManager.getLocalIpAddress() ?: return
        val message = Json.encodeToString(
            ElectionMessage(
                senderIp = ip,
                hostIp = candidates.values.find { it.isHost }?.ipAddress,
                hasGameSession = currentHasGameSession
            )
        )
        connectionManager.broadcastMessage(port = 60000, msg = message)
    }

    fun setHasGameSession(hasGameSession: Boolean) {
        currentHasGameSession = hasGameSession
    }

    suspend fun clearHost() {
        _hostIp.emit("")
    }
}

data class DeviceCandidate(
    val ipAddress: String,
    val networkNumber: Int,
    val isHost: Boolean,
    val lastSeen: Long,
    val hasGameSession: Boolean
)

@Serializable
data class ElectionMessage(
    val senderIp: String,
    val hostIp: String? = null,
    val hasGameSession: Boolean
)
