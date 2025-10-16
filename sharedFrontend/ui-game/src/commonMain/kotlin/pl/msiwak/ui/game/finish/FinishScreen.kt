package pl.msiwak.ui.game.finish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors

@Composable
fun FinishScreen(viewModel: FinishViewModel = koinInject()) {

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
                text = "FINISH ",
                color = GameColors.TextPrimary
            )
            viewState.teams?.fastForEach {
                Text(
                    text = "Team: ${it.name} - score: ${it.score}",
                    color = GameColors.TextPrimary
                )
                it.players.fastForEach { player ->
                    Text(
                        text = "Player: ${player.name} - id: ${player.score}",
                        color = GameColors.TextPrimary
                    )
                }
            }
            Button(onClick = {

            }) {
                Text(text = "Go to lobby")
            }
        }
    }
}
