@file:OptIn(ExperimentalTime::class)

package pl.msiwak.network.service

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.ConnectionManager
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ElectionService(
    private val connectionManager: ConnectionManager
) {
    private var hostElectionScope = CoroutineScope(Dispatchers.IO)
    private var electionInProgress = false
    private val candidates = hashMapOf<String, DeviceCandidate>()

    private val _hostIp = MutableStateFlow<String?>(null)
    val hostIp: StateFlow<String?> = _hostIp.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        println("ElectionService Error: ${throwable.message}")
    }

    suspend fun checkWifiIsOn() = connectionManager.checkWifiIsOn().isRunning

    suspend fun startElection() = withContext(Dispatchers.IO) {
        hostElectionScope.launch(errorHandler) {
            connectionManager.startUdpListener(port = 60000).collectLatest { message ->
                handleElectionMessage(message)
                _hostIp.value = candidates.values.find { it.isHost }?.ipAddress
                println("Candidates: ${candidates.mapValues { it.value }}")
            }
        }
        hostElectionScope.launch(errorHandler) {
            while (isActive) {
                sendMessage()
                delay(1000)
            }
        }
        hostElectionScope.launch(errorHandler) {
            while (isActive) {
                delay(3000)
                if (!electionInProgress && candidates.isNotEmpty() && candidates.values.none { it.isHost }) {
                    conductElection()
                }
                cleanupOldCandidates()
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
                lastSeen = Clock.System.now().toEpochMilliseconds()
            )
            if (hostIp != null && hostIp != senderIp) {
                candidates[hostIp] = DeviceCandidate(
                    ipAddress = hostIp,
                    networkNumber = hostIp.substringAfterLast(".").toInt(),
                    isHost = true,
                    lastSeen = candidates[hostIp]?.lastSeen ?: 0
                )
            }
        }
    }

    private fun conductElection() {
        electionInProgress = true

        val winner = candidates.values.maxByOrNull { it.networkNumber } ?: run {
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
                hostIp = candidates.values.find { it.isHost }?.ipAddress
            )
        )
        connectionManager.broadcastMessage(port = 60000, msg = message)
    }

}

data class DeviceCandidate(
    val ipAddress: String,
    val networkNumber: Int,
    val isHost: Boolean,
    val lastSeen: Long
)

@Serializable
data class ElectionMessage(
    val senderIp: String,
    val hostIp: String? = null
)
