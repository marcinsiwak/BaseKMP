package pl.msiwak.ui.createprediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.player.GetPlayersUseCase

class CreateTypeViewModel(private val getPlayersUseCase: GetPlayersUseCase) : ViewModel() {

    init {
        viewModelScope.launch {
            runCatching {
                val players = getPlayersUseCase.invoke()
                _viewState.update { it.copy(players = players) }
            }.onFailure {
                println("OUTPUT: ${it.message}")
            }
        }
    }

    private val _viewState = MutableStateFlow(CreateTypeState())
    val viewState = _viewState.asStateFlow()

    fun onUiAction(action: CreateTypeUiAction) {
        when (action) {
            is CreateTypeUiAction.PlayerPicked -> onPlayerPicked(action.pos)
            CreateTypeUiAction.CreatePrediction -> Unit
        }
    }

    private fun onPlayerPicked(pos: Int) {
        val isPickedList = viewState.value.players.filter { it.isPicked }
        viewState.value.players.mapIndexed { index, item ->
            if (index == pos) {
                if (item.isPicked) {
                    item.copy(isPicked = false)
                } else {
                    if (isPickedList.size == 5) return
                    item.copy(isPicked = true)
                }
            } else {
                item.copy()
            }
        }.run {
            _viewState.update { it.copy(players = this) }
        }
    }
}
