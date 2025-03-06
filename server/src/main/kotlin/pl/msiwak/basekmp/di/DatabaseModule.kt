package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.database.dao.Dao
import pl.msiwak.database.dao.DaoImpl

val databaseModule = module {
    single<Dao> { DaoImpl() }
}
