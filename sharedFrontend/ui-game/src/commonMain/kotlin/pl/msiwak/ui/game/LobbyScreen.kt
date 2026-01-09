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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.choose_team
import cardsthegame.sharedfrontend.common_resources.generated.resources.minimum_players_message
import cardsthegame.sharedfrontend.common_resources.generated.resources.not_ready
import cardsthegame.sharedfrontend.common_resources.generated.resources.ready
import cardsthegame.sharedfrontend.common_resources.generated.resources.wait_for_players
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CustomButton
import pl.msiwak.ui.game.component.TeamItemComponent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = koinInject()
) {
    val viewState = viewModel.uiState.collectAsState()

    BackHandler(enabled = true) {}

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = CenterHorizontally
        ) {

//            Text(
//                text = stringResource(Res.string.game_id, viewState.value.gameIpAddress ?: ""),
//                color = GameColors.OnPrimary
//            )

            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(Res.string.minimum_players_message),
                color = GameColors.OnPrimary
            )

            viewState.value.teams.fastForEach {
                TeamItemComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    teamName = it.name,
                    players = it.players,
                    isEnabled = !viewState.value.isReady
                ) {
                    viewModel.onUiAction(LobbyUiAction.JoinTeam(it.name))
                }
            }

            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = stringResource(Res.string.choose_team),
                color = GameColors.OnPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                viewState.value.playersWithoutTeam.fastForEach {
                    Text(
                        text = it.name.orEmpty(),
                        color = Color.Black
                    )
                }
            }

            if (viewState.value.isReady) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(Res.string.wait_for_players),
                    color = GameColors.OnPrimary
                )
            }

            CustomButton(
                modifier = Modifier.padding(vertical = 36.dp, horizontal = 24.dp),
                enabled = viewState.value.isButtonEnabled,
                onClick = {
                    viewModel.onUiAction(LobbyUiAction.SetReady)
                },
                text = if (viewState.value.isReady) stringResource(Res.string.not_ready) else stringResource(Res.string.ready),
            )
        }
    }
}
