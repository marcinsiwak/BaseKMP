package pl.msiwak.data.session

import pl.msiwak.network.service.SessionService

class SessionRepository(private val sessionService: SessionService) {

    suspend fun startSession() {
        sessionService.startSession()
    }

    suspend fun stopSession() {
        sessionService.stopSession()
    }
}