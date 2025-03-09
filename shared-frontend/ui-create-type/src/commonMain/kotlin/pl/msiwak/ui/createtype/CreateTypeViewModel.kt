package pl.msiwak.ui.createtype

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.msiwak.common.model.Player

class CreateTypeViewModel() : ViewModel() {

    private val _viewState = MutableStateFlow(
        CreateTypeState(
            listOf(
                Player("23", "Michael", "Jordan", "30.1"),
                Player("30", "Stephen", "Curry", "24.6"),
                Player("6", "LeBron", "James", "27.2"),
                Player("33", "Larry", "Bird", "24.3"),
                Player("24", "Kobe", "Bryant", "25.0"),
                Player("21", "Tim", "Duncan", "19.0"),
                Player("32", "Magic", "Johnson", "19.5"),
                Player("34", "Shaquille", "O'Neal", "23.7"),
                Player("13", "Wilt", "Chamberlain", "30.1"),
                Player("22", "Elgin", "Baylor", "27.4"),
                Player("20", "Manu", "Ginóbili", "13.3"),
                Player("11", "Isiah", "Thomas", "19.2"),
                Player("12", "John", "Stockton", "13.1"),
                Player("1", "Tracy", "McGrady", "19.6"),
                Player("3", "Allen", "Iverson", "26.7"),
                Player("50", "David", "Robinson", "21.1"),
                Player("7", "Kevin", "Durant", "27.3"),
                Player("41", "Dirk", "Nowitzki", "20.7"),
                Player("5", "Kevin", "Garnett", "17.8"),
                Player("2", "Kawhi", "Leonard", "19.7")
            )
        )
    )
    val viewState = _viewState.asStateFlow()

    fun onUiAction(action: CreateTypeUiAction) {
        when (action) {
            is CreateTypeUiAction.PlayerPicked -> onPlayerPicked(action.pos)
        }
    }

    private fun onPlayerPicked(pos: Int) {
        viewState.value.players.mapIndexed() { index, item ->
            if (index == pos) {
                item.copy(isPicked = true)
            } else {
                item.copy()
            }
        }.run {
            _viewState.update { it.copy(players = this) }
        }
    }
}
