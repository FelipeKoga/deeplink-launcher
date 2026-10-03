package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DuplicateDeepLinkImplTest {

    private val original = DeepLink(
        id = "a",
        link = "myapp://a",
        name = "A",
        description = "Original",
        createdAt = LocalDateTime(2026, 1, 15, 10, 30),
        isFavorite = true,
    )

    @Test
    fun mapsLinkTakenByAnotherDeepLinkToLinkAlreadyExists() = runTest {
        val repository = StubDeepLinkRepository(original, DeepLinkRepository.UpsertResult.LinkAlreadyExists("b"))

        val result = DuplicateDeepLinkImpl(repository, AcceptAllLinks)(
            deepLinkId = "a",
            newLink = "myapp://b",
            copyAllFields = true,
        )

        assertEquals(DuplicateDeepLink.Result.Error.LinkAlreadyExists, result)
    }

    @Test
    fun returnsTheSavedCopy() = runTest {
        val repository = StubDeepLinkRepository(original, DeepLinkRepository.UpsertResult.Saved)

        val result = DuplicateDeepLinkImpl(repository, AcceptAllLinks)(
            deepLinkId = "a",
            newLink = "myapp://new",
            copyAllFields = true,
        )

        val copy = assertIs<DuplicateDeepLink.Result.Success>(result).deepLink
        assertEquals("myapp://new", copy.link)
        assertEquals(listOf(copy), repository.upserted)
    }

    private object AcceptAllLinks : ValidateDeepLink {
        override fun isValid(link: String): Boolean = true
    }

    private class StubDeepLinkRepository(
        private val deepLink: DeepLink,
        private val upsertResult: DeepLinkRepository.UpsertResult,
    ) : DeepLinkRepository {
        val upserted = mutableListOf<DeepLink>()

        override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> = flowOf(deepLink.takeIf { it.id == id })

        override fun upsertDeepLink(deepLink: DeepLink): DeepLinkRepository.UpsertResult {
            upserted += deepLink
            return upsertResult
        }

        override fun getDeepLinksStream(): Flow<List<DeepLink>> = throw UnsupportedOperationException()

        override fun getDeepLinks(): List<DeepLink> = throw UnsupportedOperationException()

        override fun getDeepLinkById(id: String): DeepLink? = throw UnsupportedOperationException()

        override fun getDeepLinkByLink(link: String): DeepLink? = throw UnsupportedOperationException()

        override fun updateLastLaunchedAt(id: String, lastLaunchedAt: LocalDateTime) =
            throw UnsupportedOperationException()

        override fun deleteDeepLink(id: String) = throw UnsupportedOperationException()

        override fun deleteAll() = throw UnsupportedOperationException()
    }
}
