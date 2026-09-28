package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.datetime.LocalDateTime

private val previewNow = LocalDateTime(2026, 1, 15, 10, 30)

private val previewCreatedAt = LocalDateTime(2026, 1, 15, 8, 30)

private val previewFolder = Folder(
    id = "folder-checkout",
    name = "Checkout",
    description = null,
    deepLinkCount = 3,
)

@Composable
private fun DeepLinkCardSample(
    deepLink: DeepLink,
    actions: DeepLinkCardActions = DeepLinkCardActionsPresets.browse(onLaunch = {}, onToggleFavorite = {}),
    showFolder: Boolean = true,
) {
    DeepLinkCard(
        item = DeepLinkListItem(deepLink = deepLink),
        onClick = {},
        actions = actions,
        modifier = Modifier.fillMaxWidth(),
        showFolder = showFolder,
        now = previewNow,
    )
}

@PreviewLightDark
@Composable
private fun DeepLinkCardRelativeTimePreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-just-now",
                    link = "shop://home/feed",
                    name = "Home feed",
                    description = null,
                    createdAt = LocalDateTime(2026, 1, 15, 10, 29, 30),
                    isFavorite = false,
                ),
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-minutes",
                    link = "shop://product/42?ref=home",
                    name = "Product details",
                    description = null,
                    createdAt = LocalDateTime(2026, 1, 15, 10, 5),
                    isFavorite = false,
                ),
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-hours",
                    link = "shop://orders/1234/tracking",
                    name = "Order tracking",
                    description = null,
                    createdAt = LocalDateTime(2025, 11, 3, 9, 0),
                    isFavorite = false,
                    lastLaunchedAt = LocalDateTime(2026, 1, 15, 7, 30),
                ),
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-days",
                    link = "shop://settings/profile",
                    name = "Profile settings",
                    description = null,
                    createdAt = LocalDateTime(2026, 1, 12, 10, 30),
                    isFavorite = false,
                ),
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-older",
                    link = "shop://promo/holiday-sale",
                    name = "Holiday sale",
                    description = null,
                    createdAt = LocalDateTime(2025, 12, 24, 18, 45),
                    isFavorite = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardUnnamedPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-unnamed-short",
                    link = "shop://cart",
                    name = null,
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                ),
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-unnamed-long",
                    link = "shop://checkout/cart/items/42/variants/blue-xl?coupon=WINTER2026&source=push",
                    name = null,
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardLongNamePreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-long-name",
                    link = "shop://checkout/confirmation",
                    name = "Checkout confirmation with saved payment method, express shipping and gift wrapping",
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardFavoritePreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-favorite",
                    link = "shop://wishlist",
                    name = "Wishlist",
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = true,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardWithFolderPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-with-folder",
                    link = "shop://checkout/payment",
                    name = "Payment step",
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                    folder = previewFolder,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardFolderMemberPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-folder-member",
                    link = "shop://checkout/shipping",
                    name = "Shipping step",
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                    folder = previewFolder,
                ),
                actions = DeepLinkCardActionsPresets.folderMember(onLaunch = {}),
                showFolder = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeepLinkCardLinkPickerPreview() {
    DLLPreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-link-picker-folder",
                    link = "shop://checkout/review",
                    name = "Review order",
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                    folder = previewFolder,
                ),
                actions = DeepLinkCardActionsPresets.linkPicker,
            )
            DeepLinkCardSample(
                deepLink = DeepLink(
                    id = "deeplink-link-picker",
                    link = "shop://search?query=sneakers",
                    name = null,
                    description = null,
                    createdAt = previewCreatedAt,
                    isFavorite = false,
                ),
                actions = DeepLinkCardActionsPresets.linkPicker,
            )
        }
    }
}
