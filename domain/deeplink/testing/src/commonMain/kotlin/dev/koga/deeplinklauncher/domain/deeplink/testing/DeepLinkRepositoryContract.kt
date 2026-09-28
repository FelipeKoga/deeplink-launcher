package dev.koga.deeplinklauncher.domain.deeplink.testing

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository.InsertResult
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository.WriteResult
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

public abstract class DeepLinkRepositoryContract {

    protected abstract fun createRepositories(): Pair<DeepLinkRepository, FolderRepository>

    private val repositories by lazy { createRepositories() }
    protected val deepLinks: DeepLinkRepository get() = repositories.first
    protected val folders: FolderRepository get() = repositories.second

    @Test
    public fun editingALinkToAnotherRecordsLinkIsRejectedAndKeepsBothRecords(): Unit = runTest {
        deepLinks.insert(deepLink(id = "a", link = "demo://one"))
        deepLinks.insert(deepLink(id = "b", link = "demo://two"))

        assertEquals(WriteResult.LinkAlreadyExists, deepLinks.updateLink(id = "a", link = "demo://two"))
        assertEquals("demo://one", deepLinks.getDeepLinkById("a")?.link)
        assertEquals("demo://two", deepLinks.getDeepLinkById("b")?.link)
    }

    @Test
    public fun editingALinkToAFreeValueUpdatesOnlyThatRecord(): Unit = runTest {
        deepLinks.insert(deepLink(id = "a", link = "demo://one"))

        assertEquals(WriteResult.Success, deepLinks.updateLink(id = "a", link = "demo://new"))
        assertEquals("demo://new", deepLinks.getDeepLinkById("a")?.link)
    }

    @Test
    public fun editingAMissingRecordReportsNotFound(): Unit = runTest {
        assertEquals(WriteResult.NotFound, deepLinks.updateLink(id = "missing", link = "demo://x"))
    }

    @Test
    public fun insertingADuplicateLinkIsRejectedWithoutReplacingTheExistingRecord(): Unit = runTest {
        deepLinks.insert(deepLink(id = "a", link = "demo://one", name = "original"))

        assertEquals(InsertResult.LinkAlreadyExists, deepLinks.insert(deepLink(id = "b", link = "demo://one")))
        assertEquals("original", deepLinks.getDeepLinkById("a")?.name)
        assertNull(deepLinks.getDeepLinkById("b"))
    }

    @Test
    public fun fieldCommandsDoNotOverwriteFolderDataFromAStaleSnapshot(): Unit = runTest {
        folders.insert(Folder(id = "f", name = "Work", description = null))
        deepLinks.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f", "Work", null)))
        val staleSnapshot = deepLinks.getDeepLinkById("a")!!

        folders.update(id = "f", name = "Renamed", description = "new")
        deepLinks.setFavorite(id = staleSnapshot.id, isFavorite = true)
        deepLinks.recordLaunch(id = staleSnapshot.id, launchedAt = LocalDateTime(2026, 1, 1, 10, 0))

        assertEquals("Renamed", folders.getFolderById("f")?.name)
        val deepLink = deepLinks.getDeepLinkById("a")!!
        assertTrue(deepLink.isFavorite)
        assertEquals("f", deepLink.folder?.id)
        assertEquals("Renamed", deepLink.folder?.name)
    }

    @Test
    public fun recordingALaunchOnlyChangesLastLaunchedAt(): Unit = runTest {
        deepLinks.insert(deepLink(id = "a", link = "demo://one", name = "name"))
        val launchedAt = LocalDateTime(2026, 2, 3, 4, 5)

        deepLinks.recordLaunch(id = "a", launchedAt = launchedAt)

        val deepLink = deepLinks.getDeepLinkById("a")!!
        assertEquals(launchedAt, deepLink.lastLaunchedAt)
        assertEquals("name", deepLink.name)
        assertEquals(CREATED_AT, deepLink.createdAt)
    }

    @Test
    public fun deleteAllReturnsTheDeletedIds(): Unit = runTest {
        deepLinks.insert(deepLink(id = "a", link = "demo://one"))
        deepLinks.insert(deepLink(id = "b", link = "demo://two"))

        assertEquals(setOf("a", "b"), deepLinks.deleteAll().toSet())
        assertTrue(deepLinks.getDeepLinks().isEmpty())
    }

    @Test
    public fun renamingAFolderToATakenNameKeepsBothFoldersAndTheirLinks(): Unit = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))
        folders.insert(Folder(id = "f2", name = "Home", description = null))
        deepLinks.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f1", "Work", null)))
        deepLinks.insert(deepLink(id = "b", link = "demo://two", folder = Folder("f2", "Home", null)))

        assertEquals(
            FolderRepository.WriteResult.NameAlreadyExists,
            folders.update(id = "f1", name = "Home", description = null),
        )
        assertEquals("Work", folders.getFolderById("f1")?.name)
        assertEquals("Home", folders.getFolderById("f2")?.name)
        assertEquals("f1", deepLinks.getDeepLinkById("a")?.folder?.id)
        assertEquals("f2", deepLinks.getDeepLinkById("b")?.folder?.id)
    }

    @Test
    public fun creatingAFolderWithATakenNameIsRejected(): Unit = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))

        assertEquals(
            FolderRepository.InsertResult.NameAlreadyExists,
            folders.insert(Folder(id = "f2", name = "Work", description = null)),
        )
        assertNull(folders.getFolderById("f2"))
    }

    @Test
    public fun aMissingFolderIsNull(): Unit = runTest {
        assertNull(folders.getFolderById("missing"))
    }

    @Test
    public fun folderCountsItsDeepLinks(): Unit = runTest {
        folders.insert(Folder(id = "f", name = "Work", description = null))
        deepLinks.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f", "Work", null)))
        deepLinks.insert(deepLink(id = "b", link = "demo://two", folder = Folder("f", "Work", null)))

        assertEquals(2, folders.getFolderById("f")?.deepLinkCount)
        assertEquals(setOf("a", "b"), deepLinks.getDeepLinks().filter { it.folder?.id == "f" }.map { it.id }.toSet())
    }

    @Test
    public fun deletingAFolderUnlinksItsDeepLinks(): Unit = runTest {
        folders.insert(Folder(id = "f", name = "Work", description = null))
        deepLinks.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f", "Work", null)))

        folders.delete("f")

        assertNull(folders.getFolderById("f"))
        assertNull(deepLinks.getDeepLinkById("a")?.folder)
    }

    @Test
    public fun deletingAllFoldersUnlinksEveryDeepLink(): Unit = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))
        deepLinks.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f1", "Work", null)))

        folders.deleteAll()

        assertTrue(folders.getFolders().isEmpty())
        assertNull(deepLinks.getDeepLinkById("a")?.folder)
    }

    @Test
    public fun importUpdatesExistingLinksInPlaceAndNeverOverwritesARecordThroughItsId(): Unit = runTest {
        deepLinks.insert(deepLink(id = "local-1", link = "demo://one", name = "local name"))
        deepLinks.insert(deepLink(id = "taken", link = "demo://unrelated"))

        deepLinks.importAll(
            folders = emptyList(),
            deepLinks = listOf(
                deepLink(id = "file-1", link = "demo://one", name = "imported name"),
                deepLink(id = "taken", link = "demo://brand-new"),
            ),
        )

        assertEquals("imported name", deepLinks.getDeepLinkById("local-1")?.name)
        assertNull(deepLinks.getDeepLinkById("file-1"))
        assertEquals("demo://unrelated", deepLinks.getDeepLinkById("taken")?.link)
        assertNotEquals("taken", deepLinks.getDeepLinkByLink("demo://brand-new")?.id)
    }

    @Test
    public fun importMergesAFolderWhoseNameAlreadyExistsLocally(): Unit = runTest {
        folders.insert(Folder(id = "local-folder", name = "Work", description = null))

        deepLinks.importAll(
            folders = listOf(Folder(id = "file-folder", name = "Work", description = null)),
            deepLinks = listOf(deepLink(id = "a", link = "demo://one", folder = Folder("file-folder", "Work", null))),
        )

        assertEquals(listOf("local-folder"), folders.getFolders().map { it.id })
        assertEquals("local-folder", deepLinks.getDeepLinkById("a")?.folder?.id)
    }

    @Test
    public fun importResultDoesNotDependOnTheOrderOfFolders(): Unit = runTest {
        folders.insert(Folder(id = "L1", name = "Work", description = null))

        deepLinks.importAll(
            folders = listOf(Folder("F2", "Work", null), Folder("L1", "Home", null)),
            deepLinks = listOf(
                deepLink(id = "a", link = "demo://in-f2", folder = Folder("F2", "Work", null)),
                deepLink(id = "b", link = "demo://in-l1", folder = Folder("L1", "Home", null)),
            ),
        )

        assertEquals(setOf("Work", "Home"), folders.getFolders().map { it.name }.toSet())
        assertEquals("Work", deepLinks.getDeepLinkByLink("demo://in-f2")?.folder?.name)
        assertEquals("Home", deepLinks.getDeepLinkByLink("demo://in-l1")?.folder?.name)
    }

    @Test
    public fun importWithoutAFolderKeepsTheLocalFolderOfAnExistingLink(): Unit = runTest {
        folders.insert(Folder(id = "L1", name = "Work", description = null))
        folders.insert(Folder(id = "L9", name = "Home", description = null))
        deepLinks.insert(deepLink(id = "x", link = "demo://x", folder = Folder("L1", "Work", null)))

        deepLinks.importAll(
            folders = listOf(Folder("L1", "Home", null)),
            deepLinks = listOf(deepLink(id = "x", link = "demo://x")),
        )

        assertEquals("L1", deepLinks.getDeepLinkById("x")?.folder?.id)
    }

    @Test
    public fun importingTheSameDataTwiceIsIdempotent(): Unit = runTest {
        val folder = Folder(id = "f", name = "Work", description = "d")
        val data = listOf(
            deepLink(id = "a", link = "demo://one", folder = folder),
            deepLink(id = "b", link = "demo://two"),
        )

        deepLinks.importAll(folders = listOf(folder), deepLinks = data)
        val first = deepLinks.getDeepLinks().sortedBy { it.id }
        deepLinks.importAll(folders = listOf(folder), deepLinks = data)

        assertEquals(first, deepLinks.getDeepLinks().sortedBy { it.id })
        assertEquals(1, folders.getFolders().size)
    }

    public companion object {
        public val CREATED_AT: LocalDateTime = LocalDateTime(2025, 12, 31, 23, 59)

        public fun deepLink(
            id: String,
            link: String,
            name: String? = null,
            folder: Folder? = null,
        ): DeepLink = DeepLink(
            id = id,
            link = link,
            name = name,
            description = null,
            createdAt = CREATED_AT,
            isFavorite = false,
            folder = folder,
        )
    }
}
