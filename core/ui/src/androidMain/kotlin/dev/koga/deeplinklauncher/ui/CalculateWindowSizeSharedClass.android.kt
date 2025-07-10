package dev.koga.deeplinklauncher.ui

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

actual class CalculateWindowSizeSharedClass(private val activity: Activity) {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    actual fun invoke() = calculateWindowSizeClass(activity)
}

@Composable
actual fun calculateWindowSizeSharedClass(): WindowSizeClass {
    val activity = LocalActivity.current ?: throw IllegalStateException("No activity found")

    return CalculateWindowSizeSharedClass(activity).invoke()
}