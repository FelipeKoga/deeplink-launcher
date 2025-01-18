package dev.koga.deeplinklauncher.deeplink.api.usecase

interface GetAutoSuggestionLinks {
    operator fun invoke(link: String): List<String>
}
