package pl.msiwak

import android.app.Application
import org.koin.core.context.startKoin
import pl.msiwak.di.appModule

class AndroidApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin { modules(appModule) }
    }
}
