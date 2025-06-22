package dev.koga.deeplinklauncher.datatransfer.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

interface ImportDeepLinks {
    suspend operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): ImportDeepLinksResult
}

interface ImportDeepLinksResult {
    data object Success : ImportDeepLinksResult

    sealed interface Error : ImportDeepLinksResult {
        data class InvalidDeepLinksFound(val invalidDeepLinks: List<String>) : Error
        data object Unknown : Error
    }
}
