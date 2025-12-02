package pl.msiwak

import org.koin.core.context.startKoin
import pl.msiwak.di.appModule

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
