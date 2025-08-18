package pl.msiwak.domainimpl.session

import pl.msiwak.data.session.SessionRepository
import pl.msiwak.domain.session.StartSessionUseCase

class StartSessionUseCaseImpl(private val sessionRepository: SessionRepository) : StartSessionUseCase {
    override suspend fun invoke() {
        sessionRepository.startSession()
    }
}