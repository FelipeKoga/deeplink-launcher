package dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository

import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.domain.deeplink.testing.DeepLinkRepositoryContract
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Runs the shared contract against SQLite, plus checks that need a real database. */
class DeepLinkRepositoryImplTest : DeepLinkRepositoryContract() {

    private val db = InMemoryDatabase()

    override fun createRepositories(): Pair<DeepLinkRepository, FolderRepository> =
        DeepLinkRepositoryImpl(db.database) to FolderRepositoryImpl(db.database)

    @Test
    fun `a failure in the middle of an import rolls back everything`() = runTest {
        deepLinks.insert(deepLink(id = "local", link = "demo://local"))
        db.failInsertsOf("demo://boom")
        val folder = Folder(id = "f", name = "Imported", description = null)

        assertFailsWith<Exception> {
            deepLinks.importAll(
                folders = listOf(folder),
                deepLinks = listOf(
                    deepLink(id = "x", link = "demo://new", folder = folder),
                    deepLink(id = "y", link = "demo://boom"),
                ),
            )
        }

        // The folder and demo://new were written before the failing insert; both must be gone.
        assertNull(deepLinks.getDeepLinkByLink("demo://new"))
        assertTrue(folders.getFolders().isEmpty())
        assertEquals(listOf("demo://local"), deepLinks.getDeepLinks().map { it.link })
    }
}
