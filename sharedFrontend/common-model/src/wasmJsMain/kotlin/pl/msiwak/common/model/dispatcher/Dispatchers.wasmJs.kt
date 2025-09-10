package pl.msiwak.common.model.dispatcher

import kotlin.coroutines.CoroutineContext

actual object Dispatchers {
    actual val Main: CoroutineContext = kotlinx.coroutines.Dispatchers.Main
    actual val IO: CoroutineContext = kotlinx.coroutines.Dispatchers.Default
    actual val Default: CoroutineContext = kotlinx.coroutines.Dispatchers.Default
}
