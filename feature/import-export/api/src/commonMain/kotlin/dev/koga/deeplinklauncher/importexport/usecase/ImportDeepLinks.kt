package dev.koga.deeplinklauncher.importexport.usecase

import dev.koga.deeplinklauncher.importexport.model.FileType

interface ImportDeepLinks {
    operator fun invoke(
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
