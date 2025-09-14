package pl.msiwak.ui.game.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.koinInject
import pl.msiwak.destination.NavDestination

@Composable
fun StartScreen(
    navController: NavHostController,
    viewModel: StartViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
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
                Text(
                    text = state.value.gameIpAddress?.let { "Game IP: $it" } ?: "No game available",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = state.value.playerName,
                    onValueChange = { viewModel.onUiAction(StartUiAction.OnPlayerNameChanged(it)) },
                    label = { Text("Player Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.value.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.onUiAction(StartUiAction.CreateGame) },
                        modifier = Modifier.weight(1f),
                        enabled = !state.value.isLoading && state.value.playerName.isNotBlank()
                    ) {
                        Text("Create Game")
                    }

                    Button(
                        onClick = {
                            viewModel.onUiAction(StartUiAction.JoinGame)
                            navController.navigate(NavDestination.GameDestination.GameScreen)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !state.value.isLoading && state.value.playerName.isNotBlank()
                    ) {
                        Text("Join Game")
                    }
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
