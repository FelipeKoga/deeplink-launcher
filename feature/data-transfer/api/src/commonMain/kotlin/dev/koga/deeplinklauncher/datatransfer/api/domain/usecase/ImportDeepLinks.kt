package dev.koga.deeplinklauncher.datatransfer.api.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

public interface ImportDeepLinks {
    public suspend operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): Result

    public sealed interface Result {
        public data object Success : Result

        public sealed interface Error : Result {
            public data class InvalidDeepLinksFound(val invalidDeepLinks: List<String>) : Error
            public data object Unknown : Error
        }
    }
}
