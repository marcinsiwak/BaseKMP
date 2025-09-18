package pl.msiwak.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.koin.compose.koinInject

@Composable
fun GameScreen(
    viewModel: LobbyViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                state.value.players.fastForEach {
                    Text(
                        text = "Player ${it.name} (${it.id}) is ready: isActive: ${it.isActive}"
                    )
                }

                Button(onClick = {
                    viewModel.onUiAction(LobbyUiAction.Disconnect)
                }) {
                    Text("Disconnect from game")
                }
            }
        }
    }
}
