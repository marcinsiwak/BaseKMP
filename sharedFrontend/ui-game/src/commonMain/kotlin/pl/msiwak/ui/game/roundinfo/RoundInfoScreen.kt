package pl.msiwak.ui.game.roundinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
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
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameColors

@Composable
fun RoundInfoScreen(viewModel: RoundInfoViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Center
        ) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(color = GameColors.Primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Round: ${viewState.round}",
                    color = GameColors.OnPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = viewState.text,
                color = GameColors.OnPrimary
            )

            Text(
                modifier = Modifier,
                text = "Next player: ${viewState.currentPlayerName}",
                color = GameColors.OnPrimary
            )

            Button(
                onClick = {
                    viewModel.onUiAction(RoundInfoUiAction.OnStartRoundClick)
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(
                    backgroundColor = GameColors.ButtonPrimary,
                    contentColor = GameColors.ButtonText
                )
            ) {
                Text(
                    text = "Start",
                    color = GameColors.ButtonText
                )
            }
        }
    }
}
