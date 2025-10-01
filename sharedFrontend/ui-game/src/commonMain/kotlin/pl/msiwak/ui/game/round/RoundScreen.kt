package pl.msiwak.ui.game.round

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun RoundScreen(viewModel: RoundViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(viewState.text)
            Text("Time: ${viewState.timeRemaining}")

            if (viewState.isPlayerRound) {
                viewState.currentCard?.text?.let { Text(it) }

                Button(
                    onClick = {
                        viewModel.onUiAction(RoundUiAction.OnCorrectClick)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Correct")
                }

                Button(
                    onClick = {
                        viewModel.onUiAction(RoundUiAction.OnSkipClick)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Skip")
                }

                if (viewState.isRoundFinished) {
                    Button(
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnRoundFinished)
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Finish")
                    }
                }
            } else {
                Text("${viewState.currentPlayerName} TURN")
            }
        }
    }
}
