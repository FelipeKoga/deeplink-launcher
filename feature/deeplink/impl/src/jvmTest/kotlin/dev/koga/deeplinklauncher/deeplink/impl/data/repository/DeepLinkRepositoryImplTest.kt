package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository.UpsertResult
import dev.koga.deeplinklauncher.deeplink.impl.data.repository.RepositoryFixture.Companion.deepLink
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkRepositoryImplTest {

    private val fixture = RepositoryFixture()
    private val deepLinks = fixture.deepLinks

    @AfterTest
    fun tearDown() {
        fixture.close()
    }

    @Test
    fun rejectsLinkOfAnotherDeepLinkAndKeepsBoth() {
        val result = deepLinks.upsertDeepLink(fixture.a.copy(link = fixture.b.link, name = "Changed"))

        assertEquals(UpsertResult.LinkAlreadyExists("b"), result)
        assertEquals(fixture.a, deepLinks.getDeepLinkById("a"))
        assertEquals(fixture.b, deepLinks.getDeepLinkById("b"))
        assertEquals(3, deepLinks.getDeepLinks().size)
    }

    @Test
    fun rejectsNewDeepLinkWithTakenLink() {
        val result = deepLinks.upsertDeepLink(deepLink(id = "n", link = fixture.b.link, folder = null))

        assertEquals(UpsertResult.LinkAlreadyExists("b"), result)
        assertNull(deepLinks.getDeepLinkById("n"))
        assertEquals(fixture.b, deepLinks.getDeepLinkByLink(fixture.b.link))
        assertEquals(3, deepLinks.getDeepLinks().size)
    }

    @Test
    fun savesDeepLinkWithItsOwnLink() {
        val updated = fixture.a.copy(name = "Renamed", isFavorite = true, targetPackage = "com.example")

        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(updated))
        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(updated))
        assertEquals(updated, deepLinks.getDeepLinkById("a"))
        assertEquals(3, deepLinks.getDeepLinks().size)
    }

    @Test
    fun changesLinkToUnusedLink() {
        val updated = fixture.a.copy(link = "myapp://new")

        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(updated))
        assertNull(deepLinks.getDeepLinkByLink("myapp://a"))
        assertEquals(updated, deepLinks.getDeepLinkByLink("myapp://new"))
    }

    @Test
    fun insertsNewDeepLink() {
        val new = deepLink(id = "n", link = "myapp://n", folder = fixture.personal)

        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(new))
        assertEquals(new, deepLinks.getDeepLinkById("n"))
        assertEquals(4, deepLinks.getDeepLinks().size)
    }

    @Test
    fun staleFolderSnapshotDoesNotTouchFolders() {
        val staleA = deepLinks.getDeepLinkById("a")!!
        fixture.folders.upsertFolder(fixture.work.copy(name = "Office"))
        val foldersBefore = fixture.sortedFolders()

        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(staleA.copy(isFavorite = true)))

        assertEquals(foldersBefore, fixture.sortedFolders())
        assertEquals(fixture.work.copy(name = "Office"), deepLinks.getDeepLinkById("a")?.folder)
    }

    @Test
    fun savingDeepLinkWhoseFolderRowIsMissingCreatesNoFolder() {
        fixture.insert(deepLink(id = "d", link = "myapp://d", folder = Folder(id = "ghost", name = "", description = null)))
        val orphan = deepLinks.getDeepLinkById("d")!!
        val foldersBefore = fixture.sortedFolders()

        assertEquals(UpsertResult.Saved, deepLinks.upsertDeepLink(orphan.copy(isFavorite = true)))

        assertEquals(foldersBefore, fixture.sortedFolders())
    }
}
