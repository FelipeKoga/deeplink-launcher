package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLCodeBlock(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes
    val dimensions = DeepLinkTheme.dimensions

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.dialog)
            .background(colors.surface.card)
            .border(
                width = 1.dp,
                color = colors.border.default,
                shape = shapes.dialog,
            )
            .horizontalScroll(rememberScrollState()),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(dimensions.extraLarge),
            style = typography.code.block.copy(color = colors.text.primary),
        )
    }
}
