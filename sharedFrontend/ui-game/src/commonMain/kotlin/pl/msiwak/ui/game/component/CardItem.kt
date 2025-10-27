package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.ic_card
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_input_background
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardItem(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(Res.drawable.img_input_background),
            contentDescription = null
        )

        Text(
            modifier = Modifier.matchParentSize()
                .wrapContentHeight()
                .padding(16.dp)
                .align(Alignment.Center),
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}