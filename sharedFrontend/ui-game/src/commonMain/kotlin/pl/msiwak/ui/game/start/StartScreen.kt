package pl.msiwak.ui.game.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_backround
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.msiwak.ui.game.component.InputField

@Composable
fun StartScreen(
    viewModel: StartViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.img_backround),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Cards The Game",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                InputField(
                    value = state.value.playerName,
                    onValueChange = { viewModel.onUiAction(StartUiAction.OnPlayerNameChanged(it)) },
                    placeholder = "Player Name",
                    enabled = !state.value.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.onUiAction(StartUiAction.Join) },
                    enabled = !state.value.isLoading && state.value.playerName.isNotBlank()
                ) {
                    Text("Join")
                }

                Button(
                    onClick = { viewModel.onUiAction(StartUiAction.Refresh) },
                ) {
                    Text("Refresh")
                }

                if (state.value.isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}
