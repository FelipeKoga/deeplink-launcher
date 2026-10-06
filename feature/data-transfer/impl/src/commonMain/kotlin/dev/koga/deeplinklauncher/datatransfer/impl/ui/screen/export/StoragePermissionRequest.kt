package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export

import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberStoragePermissionRequest(onResult: (granted: Boolean) -> Unit): () -> Unit
