package pl.msiwak.ui.game.round

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.ic_card
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors

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
                    Box(
                        modifier = Modifier.padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.wrapContentSize(),
                            painter = painterResource(Res.drawable.ic_card),
                            contentDescription = "Card Image"
                        )

                        Text(
                            modifier = Modifier.matchParentSize()
                                .wrapContentHeight()
                                .padding(16.dp)
                                .align(Alignment.Center),
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
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
