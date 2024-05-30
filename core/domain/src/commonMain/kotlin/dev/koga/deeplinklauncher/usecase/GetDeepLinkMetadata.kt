package dev.koga.deeplinklauncher.usecase

expect class GetDeepLinkMetadata {

    fun execute(link: String): DeepLinkMetadata
}

data class DeepLinkMetadata(
    val link: String,
    val scheme: String?,
    val host: String?,
    val query: String?,
)
