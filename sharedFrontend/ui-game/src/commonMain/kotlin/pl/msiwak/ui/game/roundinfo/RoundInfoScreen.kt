package pl.msiwak.ui.game.roundinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun RoundInfoScreen(viewModel: RoundInfoViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(viewState.text)

            Button(
                onClick = {
                    viewModel.onUiAction(RoundInfoUiAction.OnStartRoundClick)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Start")
            }
        }
    }
}
