package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_item_backround
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(Res.drawable.img_item_backround),
            contentDescription = null
        )

        Text(
            modifier = Modifier.matchParentSize()
                .wrapContentHeight()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(24.dp))
                .clickable { onClick() }
                .padding(vertical = 16.dp)
            ,
            text = text,
            textAlign = Center
        )
    }
}