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
    val state = viewModel.uiState.collectAsState()

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
                text = "Game id: ${state.value.gameIpAddress}",
                color = GameColors.OnPrimary
            )

            state.value.teams.fastForEach {
                TeamItemComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    teamName = it.name,
                    players = it.players
                ) {
                    viewModel.onUiAction(LobbyUiAction.JoinTeam(it.name))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                state.value.playersWithoutTeam.fastForEach {
//                    Text(
//                        text = "Player ${it.name} (${it.id})\nisActive: ${it.isActive}\nisReady: ${it.isReady}",
//                        color = GameColors.OnPrimary
//                    )
//                }
//            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                state.value.playersWithoutTeam.fastForEach {
                    Text(
                        text = it.name,
                        color = Color.Black
                    )
                }
            }

            CustomButton(
                modifier = Modifier.padding(vertical = 36.dp, horizontal = 16.dp),
                onClick = {
                    viewModel.onUiAction(LobbyUiAction.SetReady)
                },
                text = "Ready"
            )
        }
    }
}
