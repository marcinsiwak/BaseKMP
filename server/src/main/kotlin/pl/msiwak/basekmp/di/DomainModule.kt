package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.UseCase

val domainModule = module {
    single { UseCase(get()) }
}
