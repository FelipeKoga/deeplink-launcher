package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

enum class DLLOutlinedChipVariant {
    Default,
    Destructive,
    Accent,
}

@Composable
fun DLLOutlinedChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: DLLOutlinedChipVariant = DLLOutlinedChipVariant.Default,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes
    val chipColors = colors.chip

    val contentColor = when (variant) {
        DLLOutlinedChipVariant.Default -> chipColors.content
        DLLOutlinedChipVariant.Destructive -> chipColors.destructiveContent
        DLLOutlinedChipVariant.Accent -> chipColors.content
    }
    val containerColor = when (variant) {
        DLLOutlinedChipVariant.Default -> chipColors.background
        DLLOutlinedChipVariant.Destructive -> chipColors.destructiveBackground
        DLLOutlinedChipVariant.Accent -> chipColors.background
    }

    val iconTint = when (variant) {
        DLLOutlinedChipVariant.Accent -> chipColors.accentContent
        else -> contentColor
    }
    val borderColor = when (variant) {
        DLLOutlinedChipVariant.Destructive -> chipColors.destructiveBorder
        else -> chipColors.border
    }

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shapes.chip,
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }

                iconPainter != null -> {
                    Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = typography.body.small.copy(
                        color = contentColor,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
