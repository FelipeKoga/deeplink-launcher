package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.Folder
import compose.icons.tablericons.Link

@Stable
class DeepLinkCardPainters internal constructor(
    internal val folder: Painter,
    internal val star: Painter,
    internal val starOutline: Painter,
    internal val launch: Painter,
    internal val linkFallback: Painter,
)

@Composable
fun rememberDeepLinkCardPainters(): DeepLinkCardPainters {
    val folder = rememberVectorPainter(TablerIcons.Folder)
    val star = rememberVectorPainter(Icons.Rounded.Star)
    val starOutline = rememberVectorPainter(Icons.Rounded.StarOutline)
    val launch = rememberVectorPainter(TablerIcons.ExternalLink)
    val linkFallback = rememberVectorPainter(TablerIcons.Link)

    return remember(folder, star, starOutline, launch, linkFallback) {
        DeepLinkCardPainters(
            folder = folder,
            star = star,
            starOutline = starOutline,
            launch = launch,
            linkFallback = linkFallback,
        )
    }
}
