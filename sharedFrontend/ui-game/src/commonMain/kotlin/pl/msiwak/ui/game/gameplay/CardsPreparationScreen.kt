package pl.msiwak.ui.game.gameplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/bowl_shake_1.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = viewState.isAnimationPlaying
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            viewModel.onUiAction(CardsPreparationUiAction.OnAnimationFinished)
        }
    }


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Image(
                modifier = Modifier.fillMaxWidth().imePadding(),
                painter = rememberLottiePainter(
                    progress = { progress },
                    composition = composition
                ),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {
                with(viewState.cardLimits) {
                    Text("Cards: $first/$second")
                }

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
