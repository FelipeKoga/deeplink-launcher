package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.FileType

expect class ExportDeepLinks {
    suspend fun export(type: FileType): ExportDeepLinksOutput
}

sealed interface ExportDeepLinksOutput {
    data object Success : ExportDeepLinksOutput
    data object Empty : ExportDeepLinksOutput
    data class Error(val throwable: Throwable) : ExportDeepLinksOutput
}