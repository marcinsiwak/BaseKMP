package pl.msiwak.globalloadermanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * GlobalLoaderManager is responsible for managing loading states across the application.
 * It provides a centralized way to show and hide loading indicators.
 */
class GlobalLoaderManager {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Shows the loading indicator.
     */
    fun showLoading() {
        _isLoading.value = true
    }

    /**
     * Hides the loading indicator.
     */
    fun hideLoading() {
        _isLoading.value = false
    }
}