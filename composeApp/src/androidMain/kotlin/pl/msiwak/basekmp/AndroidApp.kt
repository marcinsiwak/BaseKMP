package pl.msiwak.basekmp

import android.app.Application
import org.koin.core.context.startKoin
import pl.msiwak.basekmp.di.appModule

class AndroidApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin { modules(appModule) }
    }
}
