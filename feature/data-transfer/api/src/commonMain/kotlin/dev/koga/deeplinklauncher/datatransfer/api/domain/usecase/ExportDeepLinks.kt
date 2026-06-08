package dev.koga.deeplinklauncher.datatransfer.api.domain.usecase

import dev.koga.deeplinklauncher.file.model.FileType

public interface ExportDeepLinks {
    public operator fun invoke(type: FileType): Result

    public sealed interface Result {
        public data class Success(val fileName: String) : Result
        public data object Empty : Result
        public data class Error(val throwable: Throwable) : Result
    }
}
