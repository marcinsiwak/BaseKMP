package pl.msiwak.ui.game.gameplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardsPreparationScreen(viewModel: CardsPreparationViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                val composition by rememberLottieComposition {
                    LottieCompositionSpec.JsonString(
                        Res.readBytes("files/bowl.json").decodeToString()
                    )
                }
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = 2,
                    reverseOnRepeat = true,
                    restartOnPlay = true,
                    isPlaying = viewState.isAnimationPlaying
                )

                LaunchedEffect(progress) {
                    if (progress == 1f) {
                        viewModel.onUiAction(CardsPreparationUiAction.OnAnimationFinished)
                    }
                }

                Image(
                    modifier = Modifier.size(256.dp),
                    painter = rememberLottiePainter(
                        progress = { progress },
                        composition = composition
                    ),
                    contentDescription = null
                )

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
