package pl.msiwak.ui.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.player.GetPlayersUseCase

class ExampleViewModel(private val getPlayersUseCase: GetPlayersUseCase) : ViewModel() {

    init {
        viewModelScope.launch {
            runCatching {
                val players = getPlayersUseCase.invoke()
                _viewState.update { it.copy(players = players) }
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
