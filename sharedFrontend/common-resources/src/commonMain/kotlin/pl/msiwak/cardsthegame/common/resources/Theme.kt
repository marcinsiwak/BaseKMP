package pl.msiwak.basekmp.common.resources

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColors(
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

val LightColorScheme = lightColors(
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
