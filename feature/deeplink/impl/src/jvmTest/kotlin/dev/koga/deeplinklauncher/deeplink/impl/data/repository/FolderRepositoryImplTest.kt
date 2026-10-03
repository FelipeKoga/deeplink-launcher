package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository.UpsertResult
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FolderRepositoryImplTest {

    private val fixture = RepositoryFixture()
    private val folders = fixture.folders
    private val initialFolders = listOf(
        fixture.work.copy(deepLinkCount = 1),
        fixture.personal.copy(deepLinkCount = 1),
    )

    @AfterTest
    fun tearDown() {
        fixture.close()
    }

    @Test
    fun rejectsRenameToNameOfAnotherFolder() {
        val result = folders.upsertFolder(fixture.work.copy(name = "Personal"))

        assertEquals(UpsertResult.NameAlreadyExists("g"), result)
        assertEquals(initialFolders, fixture.sortedFolders())
        assertEquals(fixture.a, fixture.deepLinks.getDeepLinkById("a"))
        assertEquals(fixture.b, fixture.deepLinks.getDeepLinkById("b"))
    }

    @Test
    fun rejectsNewFolderWithTakenName() {
        val result = folders.upsertFolder(Folder(id = "n", name = "Personal", description = null))

        assertEquals(UpsertResult.NameAlreadyExists("g"), result)
        assertEquals(initialFolders, fixture.sortedFolders())
        assertEquals(fixture.b, fixture.deepLinks.getDeepLinkById("b"))
    }

    @Test
    fun insertsNewFolder() {
        val new = Folder(id = "n", name = "New", description = "Fresh")

        assertEquals(UpsertResult.Saved, folders.upsertFolder(new))
        assertEquals(initialFolders + new, fixture.sortedFolders())
    }

    @Test
    fun renamesFolderKeepingIdAndDeepLinks() {
        val renamed = fixture.work.copy(name = "Office")

        assertEquals(UpsertResult.Saved, folders.upsertFolder(renamed))
        assertEquals(listOf(renamed.copy(deepLinkCount = 1), initialFolders[1]), fixture.sortedFolders())
        assertEquals(renamed, fixture.deepLinks.getDeepLinkById("a")?.folder)
    }

    @Test
    fun savesUnchangedFolderAgain() {
        repeat(2) {
            assertEquals(UpsertResult.Saved, folders.upsertFolder(fixture.work))
        }

        assertEquals(initialFolders, fixture.sortedFolders())
    }

    @Test
    fun deleteFolderStillUnfilesDeepLinks() {
        folders.deleteFolder("f")

        assertEquals(listOf(initialFolders[1]), fixture.sortedFolders())
        assertEquals(fixture.a.copy(folder = null), fixture.deepLinks.getDeepLinkById("a"))
    }
}
