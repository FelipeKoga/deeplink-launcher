package dev.koga.deeplinklauncher.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

actual class CalculateWindowSizeSharedClass {
    @OptIn(markerClass = [ExperimentalMaterial3WindowSizeClassApi::class])
    @Composable
    actual fun invoke(): WindowSizeClass = calculateWindowSizeClass()
}

@Composable
actual fun calculateWindowSizeSharedClass(): WindowSizeClass = CalculateWindowSizeSharedClass()
    .invoke()
