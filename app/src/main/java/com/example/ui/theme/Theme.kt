package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ObsidianDarkColorScheme =
  darkColorScheme(
    primary = ElectricBlue,
    onPrimary = PrimaryText,
    primaryContainer = ElectricBlueContainer,
    onPrimaryContainer = PrimaryText,
    secondary = EmeraldGreen,
    onSecondary = ObsidianBackground,
    secondaryContainer = EmeraldGreenContainer,
    onSecondaryContainer = EmeraldGreen,
    tertiary = ElectricBlue,
    onTertiary = PrimaryText,
    background = ObsidianBackground,
    onBackground = PrimaryText,
    surface = ObsidianSurface,
    onSurface = PrimaryText,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = MutedText,
    surfaceContainerLowest = ObsidianBackground,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurface,
    surfaceContainerHigh = ObsidianSurfaceElevated,
    surfaceContainerHighest = ObsidianSurfaceElevated,
    outline = ObsidianOutline,
    outlineVariant = ObsidianOutlineVariant,
    error = CrimsonRed,
    onError = PrimaryText,
    errorContainer = CrimsonRedContainer,
    onErrorContainer = CrimsonRed,
  )

private val FallbackLightColorScheme =
  lightColorScheme(
    primary = ElectricBlue,
    onPrimary = LightSurface,
    primaryContainer = ElectricBlueContainer,
    onPrimaryContainer = PrimaryText,
    secondary = EmeraldGreen,
    onSecondary = LightSurface,
    secondaryContainer = EmeraldGreenContainer,
    onSecondaryContainer = EmeraldGreen,
    background = LightBackground,
    onBackground = LightPrimaryText,
    surface = LightSurface,
    onSurface = LightPrimaryText,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightMutedText,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    error = CrimsonRed,
    onError = LightSurface,
    errorContainer = CrimsonRedContainer,
    onErrorContainer = CrimsonRed,
  )

@Composable
fun QuickTallyTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> ObsidianDarkColorScheme
      else -> FallbackLightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
