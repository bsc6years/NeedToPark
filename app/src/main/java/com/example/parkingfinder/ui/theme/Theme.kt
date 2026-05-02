//package com.example.parkingfinder.ui.theme
//
//import android.app.Activity
//import android.os.Build
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.material3.dynamicDarkColorScheme
//import androidx.compose.material3.dynamicLightColorScheme
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.graphics.Color
//
//
///*val BrandDefault = Color(0xFF492BEE)
//val TextBrandOnBrand = Color.White // usually white on brand colors*/
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
//
//@Composable
//fun ParkingFinderTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}

package com.example.parkingfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = TextOnBrand,

    secondary = SurfaceMuted,
    onSecondary = TextPrimary,

    tertiary = BrandPrimaryLight,
    onTertiary = BrandPrimary,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = SurfaceCard,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = TextSecondary,

    outline = BorderSubtle,
    error = ErrorRed,
    onError = TextOnBrand
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = TextOnBrand,

    secondary = SurfaceMuted,
    onSecondary = TextPrimary,

    tertiary = BrandPrimaryLight,
    onTertiary = BrandPrimary,

    background = TextPrimary,
    onBackground = Color.White,

    surface = Color(0xFF2A2A34),
    onSurface = Color.White,

    surfaceVariant = Color(0xFF353543),
    onSurfaceVariant = Color(0xFFC9C9D2),

    outline = Color(0xFF5C5C68),
    error = ErrorRed,
    onError = TextOnBrand
)

@Composable
fun ParkingFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/*
Why change Theme.kt like this?
1. dynamicColor = false

This is one of the most important changes.

Because your goal is to match Figma, you do not want Android 12+ changing your palette based on wallpaper colors.

By turning this off:

your brand purple stays consistent
buttons look the same on all devices
the UI is easier to polish screen by screen
2. We now define proper Material roles

Instead of only setting primary, secondary, and tertiary, we now define:

background
surface
surfaceVariant
outline
onSurfaceVariant

That will make it much easier to style:

text fields
cards
bottom sheets
section backgrounds
supporting text
3. We keep dark mode from breaking

Even if you are mainly designing for light mode, it is still good practice not to leave dark mode half-broken.

This version gives you a usable dark mode without affecting your light-mode polish work.*/
