package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLSmallChip(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes
    val dimensions = DeepLinkTheme.dimensions
    Box(
        modifier = modifier
            .clip(shapes.chip)
            .background(colors.surface.muted)
            .clickable { onClick() }
            .padding(horizontal = dimensions.medium, vertical = dimensions.small),
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center),
            style = typography.label.field.copy(
                color = colors.text.muted,
            ),
        )
    }
}
