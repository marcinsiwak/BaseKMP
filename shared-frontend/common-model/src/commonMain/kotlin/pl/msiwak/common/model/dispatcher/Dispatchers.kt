package pl.msiwak.common.model.dispatcher

expect object Dispatchers {
    val Main: kotlin.coroutines.CoroutineContext
    val IO: kotlin.coroutines.CoroutineContext
    val Default: kotlin.coroutines.CoroutineContext
}
