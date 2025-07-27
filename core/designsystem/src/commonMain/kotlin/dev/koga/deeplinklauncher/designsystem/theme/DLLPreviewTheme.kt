package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DLLPreviewTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()

    DLLTheme(isDarkTheme = isDark) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            content()
        }
    }
}
