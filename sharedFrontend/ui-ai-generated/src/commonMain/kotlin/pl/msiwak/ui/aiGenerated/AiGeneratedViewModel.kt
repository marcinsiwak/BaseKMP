package pl.msiwak.ui.aiGenerated

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AiGeneratedViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(AiGeneratedState())
    val viewState = _viewState.asStateFlow()

    fun onUiAction(action: AiGeneratedUiAction) {
        when (action) {
            is AiGeneratedUiAction.NoAction -> Unit
        }
    }
}
