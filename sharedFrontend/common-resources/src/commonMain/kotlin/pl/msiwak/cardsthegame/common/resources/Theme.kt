package pl.msiwak.cardsthegame.common.resources

import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cardsthegame.sharedfrontend.common_resources.generated.resources.BalsamiqSans_Bold
import cardsthegame.sharedfrontend.common_resources.generated.resources.BalsamiqSans_BoldItalic
import cardsthegame.sharedfrontend.common_resources.generated.resources.BalsamiqSans_Italic
import cardsthegame.sharedfrontend.common_resources.generated.resources.BalsamiqSans_Regular
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import org.jetbrains.compose.resources.Font

val GameDarkColorScheme = darkColors(
    primary = GameColors.Primary,
    primaryVariant = GameColors.PrimaryVariant,
    secondary = GameColors.Secondary,
    secondaryVariant = GameColors.SecondaryVariant,
    background = GameColors.Background,
    surface = GameColors.Surface,
    error = GameColors.Error,
    onPrimary = GameColors.OnPrimary,
    onSecondary = GameColors.OnSecondary,
    onBackground = GameColors.OnBackground,
    onSurface = GameColors.OnSurface,
    onError = Color.White
)

val GameLightColorScheme = lightColors(
    primary = GameColors.Primary,
    primaryVariant = GameColors.PrimaryVariant,
    secondary = GameColors.Secondary,
    secondaryVariant = GameColors.SecondaryVariant,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    error = GameColors.Error,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun CustomFontFamily() = FontFamily(
    Font(Res.font.BalsamiqSans_Regular, weight = FontWeight.Light),
    Font(Res.font.BalsamiqSans_Bold, weight = FontWeight.Normal),
    Font(Res.font.BalsamiqSans_Italic, weight = FontWeight.Medium),
    Font(Res.font.BalsamiqSans_BoldItalic, weight = FontWeight.SemiBold),
)


@Composable
fun MyTypography() = Typography().run {
    val fontFamily = CustomFontFamily()
    copy(
        h1 = h1.copy(fontFamily = fontFamily),
        h2 = h2.copy(fontFamily = fontFamily),
        h3 = h3.copy(fontFamily = fontFamily),
        h4 = h4.copy(fontFamily = fontFamily),
        h5 = h5.copy(fontFamily = fontFamily),
        h6 = h6.copy(fontFamily = fontFamily),
        subtitle1 = subtitle1.copy(fontFamily = fontFamily),
        subtitle2 = subtitle2.copy(fontFamily = fontFamily),
        body1 = body1.copy(fontFamily = fontFamily),
        body2 = body2.copy(fontFamily = fontFamily),
        button = button.copy(fontFamily = fontFamily),
        caption = caption.copy(fontFamily = fontFamily),
        overline = overline.copy(fontFamily = fontFamily)
    )
}