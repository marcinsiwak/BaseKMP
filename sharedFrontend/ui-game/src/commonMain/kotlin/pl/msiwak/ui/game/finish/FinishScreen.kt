package pl.msiwak.ui.game.finish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CustomButton

@Composable
fun FinishScreen(viewModel: FinishViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

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
                text = "SCOREBOARD",
                color = GameColors.OnPrimary,
                style = MaterialTheme.typography.h3
            )
            viewState.teams?.fastForEach {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Team: ${it.name} - score: ${it.score}",
                    color = GameColors.OnPrimary
                )
                it.players.fastForEach { player ->
                    Text(
                        text = "Player: ${player.name} - ${player.score}",
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
                text = "Play again"
            )
        }
    }
}
