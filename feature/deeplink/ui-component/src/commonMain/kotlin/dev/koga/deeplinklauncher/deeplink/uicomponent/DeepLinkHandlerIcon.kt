package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.World
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DeepLinkHandlerIcon(
    iconPng: ByteArray?,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val shapes = DeepLinkTheme.shapes

    Box(
        modifier = modifier.clip(shapes.icon),
    ) {
        if (iconPng != null) {
            val imageBitmap = remember(iconPng) { iconPng.decodeToImageBitmap() }
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                imageVector = TablerIcons.World,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                tint = colors.text.muted,
            )
        }
    }
}
