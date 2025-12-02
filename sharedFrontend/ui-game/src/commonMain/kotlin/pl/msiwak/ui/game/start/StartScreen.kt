package pl.msiwak.ui.game.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.game_title
import cardsthegame.sharedfrontend.common_resources.generated.resources.join
import cardsthegame.sharedfrontend.common_resources.generated.resources.player_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.msiwak.ui.game.component.CustomButton
import pl.msiwak.ui.game.component.InputField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(
    viewModel: StartViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    BackHandler(enabled = true) {}

    Scaffold(
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.game_title),
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                InputField(
                    value = state.value.playerName,
                    onValueChange = { viewModel.onUiAction(StartUiAction.OnPlayerNameChanged(it)) },
                    placeholder = stringResource(Res.string.player_name),
                    enabled = !state.value.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(Res.string.join),
                    onClick = { viewModel.onUiAction(StartUiAction.Join) },
                    enabled = !state.value.isLoading && state.value.playerName.isNotBlank()
                )
            }

            if (state.value.isLoading) {
                CircularProgressIndicator()
            }

            if (state.value.isErrorVisible) {
                Dialog(
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                    onDismissRequest = { viewModel.onUiAction(StartUiAction.DismissDialog) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Something went wrong. Please try again",
                        )
                        Button(
                            onClick = {
                                viewModel.onUiAction(StartUiAction.DismissDialog)
                            }
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}
