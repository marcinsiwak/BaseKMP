package pl.msiwak.ui.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExampleViewModel() : ViewModel() {

    init {
        viewModelScope.launch {
            runCatching {
                // todo remove
            }.onFailure {
                println("OUTPUT: ${it.cause} ${it.message}")
            }
        }
    }

    private val _viewState = MutableStateFlow(ExampleState())
    val viewState = _viewState.asStateFlow()

    fun onUiAction(action: ExampleUiAction) {
        when (action) {
            is ExampleUiAction.PlayerPicked -> onPlayerPicked(action.pos)
            ExampleUiAction.CreateExample -> Unit
        }
    }

    private fun onPlayerPicked(pos: Int) {

    }
}
