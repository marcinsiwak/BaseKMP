package pl.msiwak.basekmp.di

import org.koin.dsl.module
import pl.msiwak.basekmp.database.dao.Dao
import pl.msiwak.basekmp.database.dao.DaoImpl

val databaseModule = module {
    single<Dao> { DaoImpl() }
}
