package dev.koga.deeplinklauncher.settings.impl.ui.products

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.purchase.api.Product
import kotlinx.collections.immutable.persistentListOf

@PreviewLightDark
@Composable
internal fun ProductsUIPreview() {
    DLLPreviewTheme {
        ProductsUI(
            products = persistentListOf(
                Product.preview,
                Product.preview,
            ),
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun ProductsUIEmptyPreview() {
    DLLPreviewTheme {
        ProductsUI(
            products = persistentListOf(),
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun ProductCardPreview() {
    DLLPreviewTheme {
        ProductCard(
            product = Product.preview,
        )
    }
}
