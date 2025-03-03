package pl.msiwak.basekmp.di

import org.koin.dsl.module
import pl.msiwak.basekmp.data.Repository
import pl.msiwak.basekmp.domain.UseCase

val repositoryModule = module {
    single { Repository(get()) }
}
