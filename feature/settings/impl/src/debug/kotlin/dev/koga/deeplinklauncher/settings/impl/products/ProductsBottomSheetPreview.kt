package dev.koga.deeplinklauncher.settings.impl.products

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.purchase.api.Product
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun ProductsBottomSheetPreview() {
    DLLPreviewTheme {
        ProductsBottomSheet(
            viewModel = koinViewModel(),
            onDismissRequest = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun ProductsUIPreview() {
    DLLPreviewTheme {
        ProductsUI(
            products = persistentListOf(
                Product.preview,
                Product.preview
            ),
            onClick = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun ProductsUIEmptyPreview() {
    DLLPreviewTheme {
        ProductsUI(
            products = persistentListOf(),
            onClick = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun ProductCardPreview() {
    DLLPreviewTheme {
        ProductCard(
            product = Product.preview
        )
    }
}