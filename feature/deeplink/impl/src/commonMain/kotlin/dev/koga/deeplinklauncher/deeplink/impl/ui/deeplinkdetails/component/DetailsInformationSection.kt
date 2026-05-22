package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkHandlerIcon
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider

@Composable
internal fun DetailsInformationContent(
    metadata: DeepLinkMetadata,
    handlerInfo: DeepLinkHandlerInfo,
    iconPng: ByteArray?,
    modifier: Modifier = Modifier,
) {
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
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
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
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Composable
private fun ResolveStatusBadge(canResolve: Boolean) {
    val backgroundColor = if (canResolve) {
        Color(0xFFE8F5E9)
    } else {
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    }
    val contentColor = if (canResolve) {
        Color(0xFF2E7D32)
    } else {
        MaterialTheme.colorScheme.error
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
    ) {
        Text(
            text = if (canResolve) "Yes" else "No",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = contentColor,
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}
