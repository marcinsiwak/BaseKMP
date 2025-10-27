package pl.msiwak.ui.game.round

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CardItem
import pl.msiwak.ui.game.component.CustomButton

@Composable
fun RoundScreen(viewModel: RoundViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = viewState.text,
                color = GameColors.OnPrimary
            )
            Text(
                text = "Time: ${viewState.timeRemaining}",
                color = GameColors.OnPrimary
            )

            if (viewState.isPlayerRound) {
                viewState.currentCard?.text?.let {
                    CardItem(
                        modifier = Modifier.padding(vertical = 32.dp),
                        text = it
                    )
                }

                if (viewState.isTimerRunning) {

                    CustomButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnCorrectClick)
                        },
                        text = "Correct"
                    )

                    CustomButton(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnSkipClick)
                        },
                        text = "Skip"
                    )
                } else {
                    CustomButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnRoundFinished)
                        },
                        text = "Finish Round"
                    )
                }
            } else {
                Text(
                    text = "${viewState.currentPlayerName} TURN",
                    color = GameColors.OnPrimary
                )
            }
        }
    }
}
