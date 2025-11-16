package pl.msiwak.ui.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_input_background
import org.jetbrains.compose.resources.painterResource

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(Res.drawable.img_input_background),
            contentDescription = null
        )

        BasicTextField(
            modifier = Modifier.matchParentSize()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current,
            maxLines = 1,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )
    }
}
