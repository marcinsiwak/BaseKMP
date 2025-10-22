package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.ic_card
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardInputItem(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


}