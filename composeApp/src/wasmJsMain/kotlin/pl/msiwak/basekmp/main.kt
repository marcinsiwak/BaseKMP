package pl.msiwak.basekmp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.koin.core.context.startKoin
import pl.msiwak.basekmp.di.appModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin { modules(appModule) }

    ComposeViewport(document.body!!) {
        App()
    }
}