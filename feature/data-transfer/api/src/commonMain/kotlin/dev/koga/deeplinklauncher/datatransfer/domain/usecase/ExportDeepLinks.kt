package dev.koga.deeplinklauncher.datatransfer.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

interface ExportDeepLinks {
    operator fun invoke(type: FileType): ExportDeepLinksResult
}

sealed interface ExportDeepLinksResult {
    data class Success(val fileName: String) : ExportDeepLinksResult
    data object Empty : ExportDeepLinksResult
    data class Error(val throwable: Throwable) : ExportDeepLinksResult
}
