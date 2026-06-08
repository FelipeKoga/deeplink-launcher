package dev.koga.deeplinklauncher.settings.impl.ui.deletedata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLButtonVariant
import dev.koga.deeplinklauncher.designsystem.button.DLLTextButton
import dev.koga.deeplinklauncher.designsystem.button.DLLTextButtonVariant
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDataBottomSheet(
    viewModel: DeleteDataViewModel,
    onDismissRequest: () -> Unit,
) {
    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        DeleteDataBottomSheetContent { deletionType ->
            viewModel.delete(deletionType)
            onDismissRequest()
        }
    }
}

@Composable
internal fun DeleteDataBottomSheetContent(
    onDelete: (DeletionType) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val dimensions = DeepLinkTheme.dimensions

    Column(
        modifier = Modifier.padding(dimensions.extraLarge).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Delete data",
            style = typography.title.sheet.copy(color = colors.text.primary),
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        Text(
            text = "Choose an option and press to confirm.",
            style = typography.body.default.copy(color = colors.text.primary),
        )

        Spacer(modifier = Modifier.height(dimensions.small))

        Text(
            text = "This action cannot be undone",
            style = typography.label.chip.copy(color = colors.text.muted),
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        DLLTextButton(
            onClick = { onDelete(DeletionType.DEEP_LINKS) },
            text = "Delete deeplinks only",
            variant = DLLTextButtonVariant.Destructive,
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        DLLTextButton(
            onClick = { onDelete(DeletionType.FOLDERS) },
            text = "Delete folders only",
            variant = DLLTextButtonVariant.Destructive,
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        DLLButton(
            onClick = { onDelete(DeletionType.ALL) },
            text = "Delete all",
            variant = DLLButtonVariant.Destructive,
        )
    }
}
