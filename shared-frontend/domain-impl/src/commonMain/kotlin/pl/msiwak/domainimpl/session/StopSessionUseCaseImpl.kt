package pl.msiwak.domainimpl.session

import pl.msiwak.data.session.SessionRepository
import pl.msiwak.domain.session.StopSessionUseCase

class StopSessionUseCaseImpl(private val sessionRepository: SessionRepository) : StopSessionUseCase {
    override suspend fun invoke() {
        sessionRepository.stopSession()
    }
}
