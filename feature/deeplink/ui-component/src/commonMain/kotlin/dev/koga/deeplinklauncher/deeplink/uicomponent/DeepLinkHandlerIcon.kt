package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Link
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DeepLinkHandlerIcon(
    icon: DeepLinkIcon?,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val shapes = DeepLinkTheme.shapes

    val iconShape = if (icon == null) CircleShape else shapes.icon

    Box(
        modifier = modifier
            .clip(iconShape)
            .then(
                if (icon == null) {
                    Modifier.background(colors.surface.muted)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            val imageBitmap = remember(icon) { icon.byteArray.decodeToImageBitmap() }
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                imageVector = TablerIcons.Link,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                tint = colors.text.muted,
            )
        }
    }
}
