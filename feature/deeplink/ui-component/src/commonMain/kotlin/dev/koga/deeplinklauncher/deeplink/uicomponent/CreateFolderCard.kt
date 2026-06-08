package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.resources.Res
import dev.koga.resources.folder_create_subtitle
import dev.koga.resources.folder_create_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateFolderCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FOLDER_CARD_HEIGHT)
            .clip(shapes.cardLarge)
            .dashedBorder(
                color = colors.border.default,
                shape = shapes.cardLarge,
            )
            .background(colors.surface.muted)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surface.background),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = TablerIcons.Plus,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = colors.text.primary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.folder_create_title),
                style = typography.title.card.copy(color = colors.text.primary),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(Res.string.folder_create_subtitle),
                style = typography.body.small.copy(color = colors.text.muted),
            )
        }
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp,
): Modifier = drawWithContent {
    drawContent()
    val stroke = strokeWidth.toPx()
    val dashPathEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
        phase = 0f,
    )
    when (val outline = shape.createOutline(size, layoutDirection, this)) {
        is Outline.Rounded -> {
            drawRoundRect(
                color = color,
                topLeft = Offset(outline.roundRect.left, outline.roundRect.top),
                size = Size(outline.roundRect.width, outline.roundRect.height),
                cornerRadius = outline.roundRect.topLeftCornerRadius,
                style = Stroke(width = stroke, pathEffect = dashPathEffect),
            )
        }

        is Outline.Rectangle -> {
            drawRect(
                color = color,
                topLeft = outline.rect.topLeft,
                size = outline.rect.size,
                style = Stroke(width = stroke, pathEffect = dashPathEffect),
            )
        }

        is Outline.Generic -> {
            drawPath(
                path = outline.path,
                color = color,
                style = Stroke(width = stroke, pathEffect = dashPathEffect),
            )
        }
    }
}
