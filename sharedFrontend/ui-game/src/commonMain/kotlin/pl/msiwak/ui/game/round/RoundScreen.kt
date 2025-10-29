package pl.msiwak.ui.game.round

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_incorrect_button
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_incorrect_button_pressed
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CustomButton
import pl.msiwak.ui.game.component.CustomClickButton

@Composable
fun RoundScreen(viewModel: RoundViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Round ${viewState.round}",
                color = GameColors.OnPrimary,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                text = if (viewState.timeRemaining > 0) "Time: ${viewState.timeRemaining}" else "Time's up!",
                color = GameColors.OnPrimary,
                style = MaterialTheme.typography.h4
            )

            if (viewState.isPlayerRound) {
                viewState.currentCard?.text?.let {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = "Your card:",
                        color = GameColors.OnPrimary,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp),
                        text = it,
                        color = GameColors.OnPrimary,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center
                    )
                }

                if (viewState.isTimerRunning) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {

                        CustomClickButton(onClick = {
                            viewModel.onUiAction(RoundUiAction.OnCorrectClick)
                        })

                        CustomClickButton(
                            onClick = {
                                viewModel.onUiAction(RoundUiAction.OnSkipClick)
                            },
                            imageNormal = painterResource(Res.drawable.img_incorrect_button),
                            imagePressed = painterResource(Res.drawable.img_incorrect_button_pressed)
                        )
                    }
                }
            } else {
                Text(
                    text = "${viewState.currentPlayerName} TURN",
                    color = GameColors.OnPrimary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!viewState.isTimerRunning) {
                CustomButton(
                    modifier = Modifier.padding(24.dp),
                    onClick = {
                        viewModel.onUiAction(RoundUiAction.OnRoundFinished)
                    },
                    text = "Finish Round"
                )
            }
        }
    }
}
