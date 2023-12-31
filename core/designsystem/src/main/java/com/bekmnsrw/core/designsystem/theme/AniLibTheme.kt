package com.bekmnsrw.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray20,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50,
)

val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

val LightAndroidGradientColors = GradientColors(container = DarkGreenGray95)

val DarkAndroidGradientColors = GradientColors(container = Color.Black)

val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

@Composable
fun AniLibTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicTheming: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = selectColorScheme(
        dynamicTheming = dynamicTheming,
        disableDynamicTheming = disableDynamicTheming,
        darkTheme = darkTheme
    )

    SystemUiColors(
        systemUiController = rememberSystemUiController(),
        colorScheme = colorScheme,
        darkTheme = darkTheme
    )

    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))

    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )

    val gradientColors = selectGradientColors(
        dynamicTheming = dynamicTheming,
        darkTheme = darkTheme,
        disableDynamicTheming = disableDynamicTheming,
        emptyGradientColors = emptyGradientColors,
        defaultGradientColors = defaultGradientColors
    )

    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp
    )

    val backgroundTheme = selectBackgroundTheme(
        dynamicTheming = dynamicTheming,
        darkTheme = darkTheme,
        defaultBackgroundTheme = defaultBackgroundTheme
    )

    val tintTheme = selectTintTheme(
        dynamicTheming = dynamicTheming,
        disableDynamicTheming = disableDynamicTheming,
        colorScheme = colorScheme
    )

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AniLibTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
private fun selectColorScheme(
    dynamicTheming: Boolean,
    disableDynamicTheming: Boolean,
    darkTheme: Boolean
): ColorScheme = when {
    dynamicTheming -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme

    !disableDynamicTheming && supportsDynamicTheming() -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context = context) else dynamicLightColorScheme(context = context)
    }

    else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
}

@Composable
private fun selectGradientColors(
    dynamicTheming: Boolean,
    darkTheme: Boolean,
    disableDynamicTheming: Boolean,
    emptyGradientColors: GradientColors,
    defaultGradientColors: GradientColors
): GradientColors = when {
    dynamicTheming -> if (darkTheme) DarkAndroidGradientColors else LightAndroidGradientColors
    !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
    else -> defaultGradientColors
}

@Composable
private fun selectBackgroundTheme(
    dynamicTheming: Boolean,
    darkTheme: Boolean,
    defaultBackgroundTheme: BackgroundTheme
): BackgroundTheme = when {
    dynamicTheming -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
    else -> defaultBackgroundTheme
}

@Composable
private fun selectTintTheme(
    dynamicTheming: Boolean,
    disableDynamicTheming: Boolean,
    colorScheme: ColorScheme
): TintTheme = when {
    dynamicTheming -> TintTheme()
    !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
    else -> TintTheme()
}

@Composable
private fun SystemUiColors(
    systemUiController: SystemUiController,
    colorScheme: ColorScheme,
    darkTheme: Boolean
) {
    SideEffect {
        systemUiController.setStatusBarColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.background,
            darkIcons = !darkTheme
        )
    }
}
