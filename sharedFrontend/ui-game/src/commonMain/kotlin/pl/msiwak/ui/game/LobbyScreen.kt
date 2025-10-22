package pl.msiwak.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.util.fastForEach
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GameColors.Background)
                .padding(top = 32.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = "Game id: ${state.value.gameIpAddress}",
                color = GameColors.OnPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                state.value.teams.fastForEach {
                    Column {
                        Text(
                            text = it.name,
                            color = GameColors.OnPrimary
                        )
                        it.players.fastForEach { player ->
                            Text(
                                text = "Player ${player.name} isReady: ${player.isReady}",
                                color = GameColors.OnPrimary
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.onUiAction(LobbyUiAction.JoinTeam(it.name))
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = GameColors.ButtonPrimary,
                                contentColor = GameColors.ButtonText
                            )
                        ) {
                            Text(
                                "Join Team ${it.name}",
                                color = GameColors.ButtonText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                state.value.playersWithoutTeam.fastForEach {
                    Text(
                        text = "Player ${it.name} (${it.id})\nisActive: ${it.isActive}\nisReady: ${it.isReady}",
                        color = GameColors.OnPrimary
                    )
                }
            }


            Button(
                onClick = {
                    viewModel.onUiAction(LobbyUiAction.SetReady)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GameColors.ButtonPrimary,
                    contentColor = GameColors.ButtonText
                )
            ) {
                Text(
                    "Ready",
                    color = GameColors.ButtonText
                )
            }

            Button(
                modifier = Modifier.padding(bottom = 32.dp),
                onClick = {
                    viewModel.onUiAction(LobbyUiAction.Disconnect)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GameColors.ButtonPrimary,
                    contentColor = GameColors.ButtonText
                )
            ) {
                Text(
                    "Disconnect from game",
                    color = GameColors.ButtonText
                )
            }
        }
    }
}
