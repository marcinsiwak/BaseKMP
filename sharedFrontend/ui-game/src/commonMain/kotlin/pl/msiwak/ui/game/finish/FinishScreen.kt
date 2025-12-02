package pl.msiwak.ui.game.finish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.play_again
import cardsthegame.sharedfrontend.common_resources.generated.resources.play_again_time
import cardsthegame.sharedfrontend.common_resources.generated.resources.player_score
import cardsthegame.sharedfrontend.common_resources.generated.resources.scoreboard
import cardsthegame.sharedfrontend.common_resources.generated.resources.team_score
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CustomButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FinishScreen(viewModel: FinishViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    BackHandler(enabled = true) {}

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(Res.string.scoreboard),
                color = GameColors.OnPrimary,
                style = MaterialTheme.typography.h3
            )
            viewState.teams?.fastForEach {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(Res.string.team_score, it.name, it.score),
                    color = GameColors.OnPrimary
                )
                it.players.fastForEach { player ->
                    Text(
                        text = stringResource(Res.string.player_score, player.name.orEmpty(), player.score),
                        color = GameColors.OnPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                onClick = {
                    viewModel.onUiAction(FinishUiAction.OnPlayAgainClicked)
                },
                enabled = viewState.timeRemaining <= 0,
                text = if (viewState.timeRemaining > 0) {
                    stringResource(Res.string.play_again_time, viewState.timeRemaining)
                } else {
                    stringResource(Res.string.play_again)
                }

            )
        }
    }
}
