package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.player.GetPlayersUseCase

val domainModule = module {
    single { GetPlayersUseCase(get()) }
}
