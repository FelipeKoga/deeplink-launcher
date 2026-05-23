package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLTextButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LinkDeepLinkToFolderBottomSheet(
    folderName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val dimensions = DeepLinkTheme.dimensions

    DLLModalBottomSheet(
        onDismiss = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
    ) {
        Column {
            Text(
                text = "Vincular deeplink",
                style = typography.title.dialog.copy(color = colors.text.primary),
                modifier = Modifier.padding(dimensions.extraLarge),
            )

            DLLHorizontalDivider()

            Text(
                text = "This deeplink was launched successfully but is not linked to \"$folderName\". " +
                    "Do you want to add it to this folder?",
                style = typography.body.default.copy(color = colors.text.primary),
                modifier = Modifier.padding(dimensions.extraLarge),
            )

            Spacer(modifier = Modifier.height(dimensions.mediumLarge))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.extraLarge),
            ) {
                DLLTextButton(
                    onClick = onDismissRequest,
                    text = "Cancel",
                    modifier = Modifier.padding(start = dimensions.mediumLarge),
                )

                Spacer(modifier = Modifier.weight(1f))

                DLLButton(
                    onClick = onConfirm,
                    text = "Vincular",
                    modifier = Modifier.padding(end = dimensions.mediumLarge),
                )
            }

            Spacer(modifier = Modifier.height(dimensions.extraLarge))
        }
    }
}
