package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository.InsertResult
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository.WriteResult
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeepLinkRepositoryImplTest {

    private val db = InMemoryDatabase()
    private val repository = DeepLinkRepositoryImpl(db.database)
    private val folders = FolderRepositoryImpl(db.database)

    @Test
    fun `editing a link to another record's link is rejected and keeps both records`() = runTest {
        repository.insert(deepLink(id = "a", link = "demo://one"))
        repository.insert(deepLink(id = "b", link = "demo://two"))

        val result = repository.updateLink(id = "a", link = "demo://two")

        assertEquals(WriteResult.LinkAlreadyExists, result)
        assertEquals("demo://one", repository.getDeepLinkById("a")?.link)
        assertEquals("demo://two", repository.getDeepLinkById("b")?.link)
    }

    @Test
    fun `editing a link to a free value updates only that record`() = runTest {
        repository.insert(deepLink(id = "a", link = "demo://one"))

        assertEquals(WriteResult.Success, repository.updateLink(id = "a", link = "demo://new"))
        assertEquals("demo://new", repository.getDeepLinkById("a")?.link)
    }

    @Test
    fun `editing a missing record reports not found`() = runTest {
        assertEquals(WriteResult.NotFound, repository.updateLink(id = "missing", link = "demo://x"))
    }

    @Test
    fun `inserting a duplicate link is rejected without replacing the existing record`() = runTest {
        repository.insert(deepLink(id = "a", link = "demo://one", name = "original"))

        val result = repository.insert(deepLink(id = "b", link = "demo://one", name = "copy"))

        assertEquals(InsertResult.LinkAlreadyExists, result)
        assertEquals("original", repository.getDeepLinkById("a")?.name)
        assertNull(repository.getDeepLinkById("b"))
    }

    @Test
    fun `field commands do not overwrite folder data from a stale snapshot`() = runTest {
        folders.insert(Folder(id = "f", name = "Work", description = null))
        repository.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f", "Work", null)))
        val staleSnapshot = repository.getDeepLinkById("a")!!

        folders.update(id = "f", name = "Renamed", description = "new")
        repository.setFavorite(id = staleSnapshot.id, isFavorite = true)
        repository.recordLaunch(id = staleSnapshot.id, launchedAt = LocalDateTime(2026, 1, 1, 10, 0))

        val folder = folders.getFolderById("f")!!
        assertEquals("Renamed", folder.name)
        assertEquals("new", folder.description)
        val deepLink = repository.getDeepLinkById("a")!!
        assertTrue(deepLink.isFavorite)
        assertEquals("f", deepLink.folder?.id)
        assertEquals("demo://one", deepLink.link)
    }

    @Test
    fun `recording a launch only changes lastLaunchedAt`() = runTest {
        repository.insert(deepLink(id = "a", link = "demo://one", name = "name"))
        val launchedAt = LocalDateTime(2026, 2, 3, 4, 5)

        repository.recordLaunch(id = "a", launchedAt = launchedAt)

        val deepLink = repository.getDeepLinkById("a")!!
        assertEquals(launchedAt, deepLink.lastLaunchedAt)
        assertEquals("name", deepLink.name)
        assertEquals(CREATED_AT, deepLink.createdAt)
    }

    @Test
    fun `deleteAll returns the deleted ids`() = runTest {
        repository.insert(deepLink(id = "a", link = "demo://one"))
        repository.insert(deepLink(id = "b", link = "demo://two"))

        assertEquals(setOf("a", "b"), repository.deleteAll().toSet())
        assertTrue(repository.getDeepLinks().isEmpty())
    }

    @Test
    fun `renaming a folder to a taken name is rejected and keeps both folders and their links`() = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))
        folders.insert(Folder(id = "f2", name = "Home", description = null))
        repository.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f1", "Work", null)))
        repository.insert(deepLink(id = "b", link = "demo://two", folder = Folder("f2", "Home", null)))

        val result = folders.update(id = "f1", name = "Home", description = null)

        assertEquals(FolderRepository.WriteResult.NameAlreadyExists, result)
        assertEquals("Work", folders.getFolderById("f1")?.name)
        assertEquals("Home", folders.getFolderById("f2")?.name)
        assertEquals("f1", repository.getDeepLinkById("a")?.folder?.id)
        assertEquals("f2", repository.getDeepLinkById("b")?.folder?.id)
    }

    @Test
    fun `creating a folder with a taken name is rejected`() = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))

        val result = folders.insert(Folder(id = "f2", name = "Work", description = null))

        assertEquals(FolderRepository.InsertResult.NameAlreadyExists, result)
        assertNull(folders.getFolderById("f2"))
    }

    @Test
    fun `deleting all folders unlinks every deeplink`() = runTest {
        folders.insert(Folder(id = "f1", name = "Work", description = null))
        repository.insert(deepLink(id = "a", link = "demo://one", folder = Folder("f1", "Work", null)))

        folders.deleteAll()

        assertTrue(folders.getFolders().isEmpty())
        assertNull(repository.getDeepLinkById("a")?.folder)
    }

    @Test
    fun `a failure in the middle of an import rolls back everything`() = runTest {
        repository.insert(deepLink(id = "local", link = "demo://local"))
        db.failInsertsOf("demo://boom")
        val folder = Folder(id = "f", name = "Imported", description = null)

        assertFailsWith<Exception> {
            repository.importAll(
                folders = listOf(folder),
                deepLinks = listOf(
                    deepLink(id = "x", link = "demo://new", folder = folder),
                    deepLink(id = "y", link = "demo://boom"),
                ),
            )
        }

        // The folder and demo://new were written before the failing insert; both must be gone.
        assertNull(repository.getDeepLinkByLink("demo://new"))
        assertTrue(folders.getFolders().isEmpty())
        assertEquals(listOf("demo://local"), repository.getDeepLinks().map { it.link })
    }

    @Test
    fun `import updates existing links in place and never overwrites a record through its id`() = runTest {
        repository.insert(deepLink(id = "local-1", link = "demo://one", name = "local name"))
        repository.insert(deepLink(id = "taken", link = "demo://unrelated"))

        repository.importAll(
            folders = emptyList(),
            deepLinks = listOf(
                deepLink(id = "file-1", link = "demo://one", name = "imported name"),
                deepLink(id = "taken", link = "demo://brand-new"),
            ),
        )

        assertEquals("imported name", repository.getDeepLinkById("local-1")?.name)
        assertNull(repository.getDeepLinkById("file-1"))
        assertEquals("demo://unrelated", repository.getDeepLinkById("taken")?.link)
        val newRecord = repository.getDeepLinkByLink("demo://brand-new")!!
        assertTrue(newRecord.id != "taken")
    }

    @Test
    fun `import merges a folder whose name already exists locally`() = runTest {
        folders.insert(Folder(id = "local-folder", name = "Work", description = null))

        repository.importAll(
            folders = listOf(Folder(id = "file-folder", name = "Work", description = null)),
            deepLinks = listOf(
                deepLink(id = "a", link = "demo://one", folder = Folder("file-folder", "Work", null)),
            ),
        )

        assertEquals(listOf("local-folder"), folders.getFolders().map { it.id })
        assertEquals("local-folder", repository.getDeepLinkById("a")?.folder?.id)
    }

    @Test
    fun `importing the same data twice is idempotent`() = runTest {
        val folder = Folder(id = "f", name = "Work", description = "d")
        val data = listOf(
            deepLink(id = "a", link = "demo://one", folder = folder),
            deepLink(id = "b", link = "demo://two"),
        )

        repository.importAll(folders = listOf(folder), deepLinks = data)
        val first = repository.getDeepLinks().sortedBy { it.id }
        repository.importAll(folders = listOf(folder), deepLinks = data)

        assertEquals(first, repository.getDeepLinks().sortedBy { it.id })
        assertEquals(1, folders.getFolders().size)
    }

    private companion object {
        val CREATED_AT = LocalDateTime(2025, 12, 31, 23, 59)

        fun deepLink(
            id: String,
            link: String,
            name: String? = null,
            folder: Folder? = null,
        ) = DeepLink(
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
