package pl.msiwak.ui.game.roundinfo

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.next_player
import cardsthegame.sharedfrontend.common_resources.generated.resources.round
import cardsthegame.sharedfrontend.common_resources.generated.resources.start
import pl.msiwak.cardsthegame.common.resources.GameColors
import pl.msiwak.ui.game.component.CustomButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RoundInfoScreen(viewModel: RoundInfoViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    BackHandler(enabled = true) {}

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 64.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Center
        ) {
            Text(
                text = stringResource(Res.string.round, viewState.round),
                color = GameColors.OnPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                text = viewState.text,
                color = GameColors.OnPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier,
                text = "Next player: ${viewState.currentPlayerName}",
                color = GameColors.OnPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            if (viewState.isPlayerRound) {
                CustomButton(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                    text = "Start",
                    onClick = {
                        viewModel.onUiAction(RoundInfoUiAction.OnStartRoundClick)
                    }
                )
            }
        }
    }
}
