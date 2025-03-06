package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.Repository
import pl.msiwak.domain.UseCase

val repositoryModule = module {
    single { Repository(get()) }
}
