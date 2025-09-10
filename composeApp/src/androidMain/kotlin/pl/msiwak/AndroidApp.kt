package pl.msiwak

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.msiwak.common.AppContext
import pl.msiwak.di.appModule
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.ConnectionManagerImpl
import pl.msiwak.network.KtorServer
import pl.msiwak.network.KtorServerImpl

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)

        startKoin {
            modules(appModule + platformModule)
        }
    }
}

val platformModule = module {
    single<KtorServer> { KtorServerImpl() }
    single<ConnectionManager> { ConnectionManagerImpl() }
}
