package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
public interface LinkDeepLinkToFolder {
    public suspend operator fun invoke(deepLinkId: String, folderId: String): Result

    public sealed interface Result {
        public data object Linked : Result
        public data object AlreadyLinked : Result
        public data object NotFound : Result
    }
}
