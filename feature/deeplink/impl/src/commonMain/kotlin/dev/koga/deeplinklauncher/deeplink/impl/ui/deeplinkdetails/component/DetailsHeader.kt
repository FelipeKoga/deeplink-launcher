package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.date.format
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkHandlerIcon
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

private const val addedAtDateFormat = "MMM d, yyyy 'at' h:mm a"

@Composable
internal fun DetailsHeader(
    uiState: DeepLinkDetailsUiState.Launch,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val addedAtText = "Added ${uiState.deepLink.createdAt.format(addedAtDateFormat)}"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        DeepLinkHandlerIcon(
            icon = uiState.details.icon,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiState.details.displayName,
                style = typography.title.card.copy(
                    color = colors.text.primary,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = addedAtText,
                style = typography.body.small.copy(
                    color = colors.text.muted,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
