package pl.msiwak.ui.game.gameplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_send_button
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_send_button_pressed
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import cardsthegame.sharedfrontend.common_resources.generated.resources.card_idea_placeholder
import cardsthegame.sharedfrontend.common_resources.generated.resources.cards_count
import pl.msiwak.ui.game.component.CustomClickButton
import pl.msiwak.ui.game.component.InputField

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CardsPreparationScreen(viewModel: CardsPreparationViewModel = koinInject()) {

    val viewState = viewModel.uiState.collectAsState().value

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/bowl_shake.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = viewState.isAnimationPlaying
    )

    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            viewModel.onUiAction(CardsPreparationUiAction.OnAnimationFinished)
        }
    }

    BackHandler(enabled = true) {}

    Scaffold(
        modifier = Modifier.imePadding(),
        backgroundColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 32.dp)
                .onSizeChanged {
                    coroutineScope.launch {
                        delay(100)
                        bringIntoViewRequester.bringIntoView()
                    }
                }
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = rememberLottiePainter(
                    progress = { progress },
                    composition = composition
                ),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                with(viewState.cardLimits) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(Res.string.cards_count, first, second),
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InputField(
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        value = viewState.text,
                        onValueChange = { viewModel.onUiAction(CardsPreparationUiAction.OnTextInput(it)) },
                        placeholder = stringResource(Res.string.card_idea_placeholder),
                    )
                    CustomClickButton(
                        modifier = Modifier.height(IntrinsicSize.Min).bringIntoViewRequester(bringIntoViewRequester),
                        onClick = {
                            viewModel.onUiAction(CardsPreparationUiAction.OnAddCardClicked)
                        },
                        enabled = viewState.isSendEnabled,
                        imageNormal = painterResource(Res.drawable.img_send_button),
                        imagePressed = painterResource(Res.drawable.img_send_button_pressed)
                    )
                }
            }
        }
    }
}
