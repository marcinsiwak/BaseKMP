package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_item_backround
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomBackground(
    modifier: Modifier = Modifier,
    content: @Composable (Dp) -> Unit
) {
    val density = LocalDensity.current
    var height by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier.height(IntrinsicSize.Max),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize()
                .onGloballyPositioned {
                    height = it.size.height
                },
            painter = painterResource(Res.drawable.img_item_backround),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        with(density) {
            content(height.toDp())
        }
    }
}