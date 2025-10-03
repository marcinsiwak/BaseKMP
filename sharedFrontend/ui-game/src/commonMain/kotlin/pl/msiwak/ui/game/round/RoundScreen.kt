package pl.msiwak.ui.game.round

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CardItem

@Composable
fun RoundScreen(viewModel: RoundViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GameColors.Background)
                .padding(top = 32.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = viewState.text,
                color = GameColors.TextPrimary
            )
            Text(
                text = "Time: ${viewState.timeRemaining}",
                color = GameColors.TextPrimary
            )

            if (viewState.isPlayerRound) {
                viewState.currentCard?.text?.let {
                    CardItem(
                        modifier = Modifier.padding(vertical = 32.dp),
                        text = it
                    )
                }

                if (viewState.isTimerRunning) {

                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnCorrectClick)
                        },
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(
                            backgroundColor = GameColors.ButtonPrimary,
                            contentColor = GameColors.ButtonText
                        )
                    ) {
                        Text(
                            text = "Correct",
                            color = GameColors.ButtonText
                        )
                    }

                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnSkipClick)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GameColors.ButtonSecondary,
                            contentColor = GameColors.ButtonText
                        )
                    ) {
                        Text(
                            text = "Skip",
                            color = GameColors.ButtonText
                        )
                    }
                } else {
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.onUiAction(RoundUiAction.OnRoundFinished)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GameColors.ButtonPrimary,
                            contentColor = GameColors.ButtonText
                        )
                    ) {
                        Text(
                            text = "Finish",
                            color = GameColors.ButtonText
                        )
                    }
                }
            } else {
                Text(
                    text = "${viewState.currentPlayerName} TURN",
                    color = GameColors.TextPrimary
                )
            }
        }
    }
}
