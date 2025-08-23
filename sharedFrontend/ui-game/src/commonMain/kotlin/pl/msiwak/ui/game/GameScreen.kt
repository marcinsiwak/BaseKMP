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
import org.koin.compose.koinInject

@Composable
fun GameScreen(
    viewModel: GameViewModel = koinInject()
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
                Text("Connected Players: ${state.value.players.joinToString(", ")}")

                Button(onClick = {
                    viewModel.onUiAction(GameUiAction.StartSession)
                }) {
                    Text("Start Game")
                }
                Button(onClick = {
                    viewModel.onUiAction(GameUiAction.StopSession)
                }) {
                    Text("Stop Game")
                }
                Button(onClick = {
                    viewModel.onUiAction(GameUiAction.Connect)
                }) {
                    Text("Connect to game")
                }
            }
        }
    }
}
