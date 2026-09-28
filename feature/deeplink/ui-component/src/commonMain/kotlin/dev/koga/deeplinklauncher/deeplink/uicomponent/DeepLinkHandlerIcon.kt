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
import androidx.compose.ui.graphics.ImageBitmap
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
            val imageBitmap = remember(icon) { HandlerIconBitmapCache.get(icon) }
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

private object HandlerIconBitmapCache {
    private const val MAX_ENTRIES = 32

    private val bitmaps = LinkedHashMap<Long, ImageBitmap>()

    fun get(icon: DeepLinkIcon): ImageBitmap {
        bitmaps.remove(icon.id)?.let { cached ->
            bitmaps[icon.id] = cached
            return cached
        }

        val bitmap = icon.byteArray.decodeToImageBitmap()
        bitmaps[icon.id] = bitmap
        if (bitmaps.size > MAX_ENTRIES) {
            bitmaps.remove(bitmaps.keys.first())
        }
        return bitmap
    }
}
