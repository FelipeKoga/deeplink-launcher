package dev.koga.deeplinklauncher.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

expect class CalculateWindowSizeSharedClass {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun invoke(): WindowSizeClass
}

@Composable
expect fun calculateWindowSizeSharedClass(): WindowSizeClass
