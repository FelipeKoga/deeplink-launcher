package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun EditableTextViewModePreview() {
    DLLPreviewTheme {
        EditableText(
            value = "Value",
            onSave = {},
            inputLabel = "Label",
            textContent = {
                Text("Text Content")
            },
        )
    }
}

@PreviewLightDark
@Composable
internal fun EditableTextEditModeEnabledPreview() {
    DLLPreviewTheme {
        EditableText(
            value = "Value",
            onSave = {},
            inputLabel = "Label",
            editButtonEnabled = true,
            initiallyInEditMode = true,
            textContent = {
                Text("Text Content")
            },
        )
    }
}

@PreviewLightDark
@Composable
internal fun EditableTextEditModeDisabledPreview() {
    DLLPreviewTheme {
        EditableText(
            value = "Value",
            onSave = {},
            inputLabel = "Label",
            editButtonEnabled = false,
            initiallyInEditMode = true,
            textContent = {
                Text("Text Content")
            },
        )
    }
}
