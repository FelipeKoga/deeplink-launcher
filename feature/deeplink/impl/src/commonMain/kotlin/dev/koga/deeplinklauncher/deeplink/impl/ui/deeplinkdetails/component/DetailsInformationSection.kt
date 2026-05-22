package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkHandlerIcon
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
internal fun DetailsInformationContent(
    metadata: DeepLinkMetadata,
    handlerInfo: DeepLinkHandlerInfo,
    iconPng: ByteArray?,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val showPath = metadata.path?.let { it != "/" && it.isNotBlank() } == true
    val targetAppName = handlerInfo.appName

    Column(modifier = modifier.fillMaxWidth()) {
        InfoRow(
            label = "Scheme",
            value = metadata.scheme ?: "—",
        )

        InfoDivider()

        InfoRow(
            label = "Host",
            value = metadata.host ?: "—",
        )

        if (showPath) {
            InfoDivider()

            InfoRow(
                label = "Path",
                value = metadata.path ?: "/",
            )
        }

        if (!metadata.query.isNullOrBlank()) {
            InfoDivider()

            InfoRow(
                label = "Query",
                value = metadata.query.orEmpty(),
            )
        }

        InfoDivider()

        InfoRow(
            label = "Can resolve",
            valueContent = {
                ResolveStatusBadge(canResolve = handlerInfo.canResolve)
            },
        )

        if (targetAppName != null) {
            InfoDivider()

            InfoRow(
                label = "Target app",
                valueContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DeepLinkHandlerIcon(
                            iconPng = iconPng,
                            modifier = Modifier.size(20.dp),
                        )

                        Text(
                            text = targetAppName,
                            style = typography.body.smallEmphasis.copy(
                                color = colors.text.primary,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun InfoDivider() {
    DLLHorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
}

@Composable
private fun InfoRow(
    label: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    valueContent: (@Composable () -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = typography.body.small.copy(
                color = colors.text.muted,
            ),
            modifier = Modifier.padding(end = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            modifier = Modifier.weight(1f),
        ) {
            if (valueContent != null) {
                valueContent()
            } else {
                Text(
                    text = value.orEmpty(),
                    style = typography.body.smallEmphasis.copy(
                        color = colors.text.primary,
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = colors.text.muted.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Composable
private fun ResolveStatusBadge(canResolve: Boolean) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val shapes = DeepLinkTheme.shapes
    val backgroundColor = if (canResolve) {
        colors.status.successBackground
    } else {
        colors.status.errorBackground.copy(alpha = 0.5f)
    }
    val contentColor = if (canResolve) {
        colors.status.successContent
    } else {
        colors.status.errorContent
    }

    Surface(
        shape = shapes.medium,
        color = backgroundColor,
    ) {
        Text(
            text = if (canResolve) "Yes" else "No",
            style = typography.label.badge.copy(
                color = contentColor,
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}
