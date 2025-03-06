package pl.msiwak.domain

import pl.msiwak.data.Repository

class UseCase(val repository: Repository) {
    // setup use cases here
    operator fun invoke() {
    }
}