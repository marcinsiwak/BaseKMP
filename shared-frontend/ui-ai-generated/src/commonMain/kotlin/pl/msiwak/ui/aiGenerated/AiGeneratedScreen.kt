package pl.msiwak.ui.aiGenerated

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject

@Composable
fun AiGeneratedScreen(viewModel: AiGeneratedViewModel = koinInject()) {
    AiGeneratedScreenContent(
        viewState = viewModel.viewState.collectAsState().value,
        onUiAction = viewModel::onUiAction
    )
}

@Composable
fun AiGeneratedScreenContent(
    viewState: AiGeneratedState,
    onUiAction: (AiGeneratedUiAction) -> Unit
) {
    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewState.message,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}
