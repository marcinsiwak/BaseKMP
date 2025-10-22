package pl.msiwak.ui.game.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val PlaceholderTextColor = Color(0xFF888888) // Medium gray for placeholder
val InputTextColor = Color(0xFF333333) // Dark gray for actual input text

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = PlaceholderTextColor,
                fontSize = 16.sp // Adjust font size as needed
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp) // Example padding from screen edges
            .clip(RoundedCornerShape(24.dp)), // Rounded corners for the entire field
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = InputTextColor,
            cursorColor = InputTextColor, // Color of the blinking cursor
            focusedIndicatorColor = Color.Transparent, // Removes default underline
            unfocusedIndicatorColor = Color.Transparent, // Removes default underline
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        enabled = enabled,
        shape = RoundedCornerShape(24.dp) // Redundant with clip, but good for consistency
    )
}
