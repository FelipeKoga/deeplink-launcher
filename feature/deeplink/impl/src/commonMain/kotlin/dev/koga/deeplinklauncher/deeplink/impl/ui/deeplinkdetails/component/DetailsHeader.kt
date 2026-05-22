package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.date.format
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkHandlerIcon
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.datetime.LocalDateTime

private const val addedAtDateFormat = "MMM d, yyyy 'at' h:mm a"

@Composable
internal fun DetailsHeader(
    deepLink: DeepLink,
    iconPng: ByteArray?,
    metadataHost: String?,
    createdAt: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val customName = deepLink.name?.takeIf { it.isNotBlank() }
    val title = customName ?: metadataHost ?: deepLink.link
    val addedAtText = "Added ${createdAt.format(addedAtDateFormat)}"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        DeepLinkHandlerIcon(
            iconPng = iconPng,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colors.text.primary,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = addedAtText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.text.muted,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
