package pl.msiwak

import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.msiwak.di.appModule
import pl.msiwak.network.KtorServer

fun initKoin() {
    startKoin {
        modules(appModule + platformModule)
    }
}

val platformModule = module {
    single<KtorServer> { KtorServerImpl() }
}
