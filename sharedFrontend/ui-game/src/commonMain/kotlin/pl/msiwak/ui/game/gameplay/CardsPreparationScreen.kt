package pl.msiwak.ui.game.gameplay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun CardsPreparationScreen(viewModel: CardsPreparationViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                TextField(value = viewState.text, onValueChange = {
                    viewModel.onUiAction(CardsPreparationUiAction.OnTextInput(it))
                })

                Button(
                    onClick = {
                        viewModel.onUiAction(CardsPreparationUiAction.OnAddCardClicked)
                    },
                ) {
                    Text("Add card")
                }

            }
        }
    }
}
