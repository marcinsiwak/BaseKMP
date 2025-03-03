package pl.msiwak.basekmp.di

import org.koin.dsl.module
import pl.msiwak.basekmp.domain.UseCase

val domainModule = module {
    single { UseCase(get()) }
}
