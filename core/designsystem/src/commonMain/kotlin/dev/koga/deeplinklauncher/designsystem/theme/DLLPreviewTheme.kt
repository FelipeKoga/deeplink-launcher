package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun DLLPreviewTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()

    DLLTheme(isDarkTheme = isDark) {
        val background = DeepLinkTheme.colors.surface.background

        CompositionLocalProvider(LocalContentColor provides contentColorFor(background)) {
            Column(modifier = Modifier.background(color = background)) {
                content()
            }
        }
    }
}
