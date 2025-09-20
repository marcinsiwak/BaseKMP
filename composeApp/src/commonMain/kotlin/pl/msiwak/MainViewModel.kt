package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.navigator.Navigator

class MainViewModel(
    val navigator: Navigator,
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            observeWebSocketEventsUseCase()
        }
    }
}

