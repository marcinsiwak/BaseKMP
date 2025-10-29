package pl.msiwak.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import pl.msiwak.ui.game.component.TeamItemComponent

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = koinInject()
) {
    val viewState = viewModel.uiState.collectAsState()

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = "Game id: ${viewState.value.gameIpAddress}",
                color = GameColors.OnPrimary
            )

            viewState.value.teams.fastForEach {
                TeamItemComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    teamName = it.name,
                    players = it.players
                ) {
                    viewModel.onUiAction(LobbyUiAction.JoinTeam(it.name))
                }
            }

            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = "Choose a team",
                color = GameColors.OnPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                viewState.value.playersWithoutTeam.fastForEach {
                    Text(
                        text = it.name,
                        color = Color.Black
                    )
                }
            }

            if (viewState.value.isReady) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Wait for other players...",
                    color = GameColors.OnPrimary
                )
            }

            CustomButton(
                modifier = Modifier.padding(vertical = 36.dp, horizontal = 16.dp),
                onClick = {
                    viewModel.onUiAction(LobbyUiAction.SetReady)
                },
                text = if (viewState.value.isReady) "Not ready" else "Ready",
            )
        }
    }
}
