package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditableTextEnabledPreview() {
    EditableText(
        value = "Value",
        onSave = {},
        inputLabel = "Label",
        editButtonEnabled = true,
        textContent = {
            Text("Text Content")
        }
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditableTextDisabledPreview() {
    EditableText(
        value = "Value",
        onSave = {},
        inputLabel = "Label",
        editButtonEnabled = false,
        textContent = {
            Text("Text Content")
        }
    )
}