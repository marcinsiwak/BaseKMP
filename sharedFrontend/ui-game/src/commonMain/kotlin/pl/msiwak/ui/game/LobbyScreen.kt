package pl.msiwak.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import org.koin.compose.koinInject

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Game id: ${state.value.gameIpAddress}"
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(text = "Team A")
                    state.value.players.filter { player ->
                        state.value.teams.find { it.name == "A" }?.playerIds?.contains(
                            player.id
                        ) == true
                    }.fastForEach {
                        Text(
                            text = "Player ${it.name}"
                        )
                    }
                    Button(onClick = {
                        viewModel.onUiAction(LobbyUiAction.JoinTeam("A"))
                    }) {
                        Text("Join Team A")
                    }
                }
                Column {
                    Text(text = "Team B")
                    state.value.players.filter { player ->
                        state.value.teams.find { it.name == "B" }?.playerIds?.contains(
                            player.id
                        ) == true
                    }.fastForEach {
                        Text(
                            text = "Player ${it.name}"
                        )
                    }
                    Button(onClick = {
                        viewModel.onUiAction(LobbyUiAction.JoinTeam("B"))
                    }) {
                        Text("Join Team B")
                    }
                }
            }

            state.value.players.fastForEach {
                Text(
                    text = "Player ${it.name} (${it.id}) isActive: ${it.isActive} isReady: ${it.isReady}"
                )
            }

            Button(onClick = {
                viewModel.onUiAction(LobbyUiAction.SetReady)
            }) {
                Text("Ready")
            }

            Button(onClick = {
                viewModel.onUiAction(LobbyUiAction.Disconnect)
            }) {
                Text("Disconnect from game")
            }
        }
    }
}
