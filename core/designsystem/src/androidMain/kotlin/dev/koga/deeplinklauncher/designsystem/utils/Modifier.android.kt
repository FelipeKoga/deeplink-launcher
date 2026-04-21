package dev.koga.deeplinklauncher.designsystem.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

actual fun Modifier.testTagAsResourceId(): Modifier {
    return semantics {
        testTagsAsResourceId = true
    }
}
