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
    private val _globalLoaderState = MutableStateFlow(GlobalLoaderManagerState())
    val globalLoaderState: StateFlow<GlobalLoaderManagerState> = _globalLoaderState.asStateFlow()

    /**
     * Shows the loading indicator.
     */
    fun showLoading(message: String? = null) {
        _globalLoaderState.update { it.copy(isLoading = true, message = message) }
    }

    /**
     * Hides the loading indicator.
     */
    fun hideLoading() {
        _globalLoaderState.update { it.copy(isLoading = false) }
    }
}

data class GlobalLoaderManagerState(val isLoading: Boolean = false, val message: String? = null)
