package pl.msiwak.basekmp

import org.koin.core.context.startKoin
import pl.msiwak.basekmp.di.appModule

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
