package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_correct_button
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_correct_button_pressed
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomClickButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageNormal: Painter = painterResource(Res.drawable.img_correct_button),
    imagePressed: Painter = painterResource(Res.drawable.img_correct_button_pressed),
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier.size(height = 128.dp, width = 156.dp).padding(start = 16.dp),
            painter = if (isPressed) imagePressed else imageNormal,
            contentDescription = null
        )
    }
}