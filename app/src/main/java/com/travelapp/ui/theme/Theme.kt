package com.travelapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.ViewCompat
import com.travelapp.R

private val DarkColorScheme = darkColorScheme(
//    primary = Color.White,
//    onPrimary = Color.White,
//    primaryContainer = Color.White,
//    onPrimaryContainer = Color.White,
//    inversePrimary = Color.White,
//    secondary = Color.White,
//    onSecondary = Color.White,
//    secondaryContainer = Color.White,
//    onSecondaryContainer = Color.White,
//    tertiary = Color.White,
//    onTertiary = Color.White,
//    tertiaryContainer = Color.White,
//    onTertiaryContainer = Color.White,
//    background = Color.White,
//    onBackground = Color.White,
//    surface = Color.White,
//    onSurface = Color.White,
//    surfaceVariant = Color.White,
//    onSurfaceVariant = Color.White,
//    surfaceTint = Color.White,
//    inverseSurface = Color.White,
//    inverseOnSurface = Color.White,
//    error = Color.White,
//    onError = Color.White,
//    errorContainer = Color.White,
//    onErrorContainer = Color.White,
//    outline = Color.White,
//    outlineVariant = Color.White,
//    scrim = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = BackgroundAccentColor,
    secondary = TuftsBlue,
    tertiary = Khaki,
    background = BackgroundColor,
    surface = BackgroundColor,
)

val marsFamily = FontFamily(
    Font(R.font.mars_brands, FontWeight.Normal)
)

val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_light_italic, FontWeight.Light, FontStyle.Italic)
)

val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_light_italic, FontWeight.Light, FontStyle.Italic),
)

val halcomFamily = FontFamily(
    Font(R.font.halcom_regular, FontWeight.Normal),
    Font(R.font.halcom_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.halcom_bold, FontWeight.Bold),
    Font(R.font.halcom_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.halcom_light, FontWeight.Light),
    Font(R.font.halcom_light_italic, FontWeight.Light, FontStyle.Italic),
)

@Composable
fun TravelAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}