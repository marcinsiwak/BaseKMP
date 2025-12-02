package pl.msiwak.globalloadermanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * GlobalLoaderManager is responsible for managing loading states across the application.
 * It provides a centralized way to show and hide loading indicators.
 */
class GlobalLoaderManager {
    private val _globalLoaderState = MutableStateFlow(GlobalLoaderData())
    val globalLoaderState: StateFlow<GlobalLoaderData> = _globalLoaderState.asStateFlow()

    /**
     * Shows the loading indicator.
     */
    fun showLoading(messageType: GlobalLoaderMessageType) {
        _globalLoaderState.update { it.copy(isLoading = true, messageType = messageType) }
    }

    /**
     * Hides the loading indicator.
     */
    fun hideLoading() {
        _globalLoaderState.update { it.copy(isLoading = false) }
    }
}

data class GlobalLoaderData(val isLoading: Boolean = false, val messageType: GlobalLoaderMessageType? = null)

enum class GlobalLoaderMessageType {
    MISSING_HOST, DEFAULT
}
