package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.model.FileType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ImportDeepLinksImplTest {

    private val repository = RecordingRepository()
    private val shortcuts = RecordingShortcutManager()
    private val files = mutableListOf<File>()
    private val import = ImportDeepLinksImpl(
        getFileContent = GetFileContent(),
        deepLinkRepository = repository,
        validateDeepLink = object : ValidateDeepLink {
            override fun isValid(link: String): Boolean = link.contains("://")
        },
        shortcutManager = shortcuts,
    )

    @AfterTest
    fun cleanUp() {
        files.forEach(File::delete)
    }

    @Test
    fun `a malformed date in a later entry fails before anything is written`() = runTest {
        val json = """
            {
              "folders": [{ "id": "f", "name": "Work" }],
              "deepLinks": [
                { "link": "demo://one", "folderId": "f", "createdAt": "2026-01-01T10:00:00" },
                { "link": "demo://two", "createdAt": "not-a-date" }
              ]
            }
        """.trimIndent()

        val result = import(file(json, "json"), FileType.JSON)

        assertEquals(ImportDeepLinks.Result.Error.Unknown, result)
        assertNull(repository.imported)
    }

    @Test
    fun `invalid links are reported and nothing is written`() = runTest {
        val json = """{ "deepLinks": [{ "link": "demo://ok" }, { "link": "broken" }] }"""

        val result = import(file(json, "json"), FileType.JSON)

        assertEquals(ImportDeepLinks.Result.Error.InvalidDeepLinksFound(listOf("broken")), result)
        assertNull(repository.imported)
    }

    @Test
    fun `a valid json file is applied in a single import`() = runTest {
        val json = """
            {
              "folders": [{ "id": "f", "name": "Work" }],
              "deepLinks": [{ "id": "a", "link": "demo://one", "folderId": "f", "createdAt": "2026-01-01T10:00:00" }]
            }
        """.trimIndent()

        val result = import(file(json, "json"), FileType.JSON)

        assertEquals(ImportDeepLinks.Result.Success, result)
        val (folders, deepLinks) = repository.imported!!
        assertEquals(listOf("f"), folders.map(Folder::id))
        assertEquals("f", deepLinks.single().folder?.id)
        assertEquals(LocalDateTime(2026, 1, 1, 10, 0), deepLinks.single().createdAt)
        assertEquals(listOf("a"), shortcuts.enabled)
    }

    @Test
    fun `text import ignores blank lines, carriage returns and duplicates`() = runTest {
        val result = import(file("demo://one\r\n\r\ndemo://two\r\ndemo://one\r\n", "txt"), FileType.TXT)

        assertIs<ImportDeepLinks.Result.Success>(result)
        assertEquals(listOf("demo://one", "demo://two"), repository.imported!!.second.map(DeepLink::link))
    }

    private fun file(content: String, extension: String): String =
        File.createTempFile("import", ".$extension").apply { writeText(content) }.also(files::add).absolutePath

    private class RecordingShortcutManager : DeepLinkShortcutManager {
        val enabled = mutableListOf<String>()

        override suspend fun enable(deepLinkIds: List<String>) {
            enabled += deepLinkIds
        }

        override suspend fun isAdded(deepLinkId: String): Boolean = false
        override suspend fun add(deepLink: DeepLink): DeepLinkShortcutManager.AddResult =
            DeepLinkShortcutManager.AddResult.NotSupported
        override suspend fun update(deepLink: DeepLink) = Unit
        override suspend fun remove(deepLinkId: String) = Unit
        override suspend fun disable(deepLinkIds: List<String>) = Unit
    }

    /** Records the single importAll call; every other write fails the test. */
    private class RecordingRepository : DeepLinkRepository {
        var imported: Pair<List<Folder>, List<DeepLink>>? = null

        override suspend fun importAll(folders: List<Folder>, deepLinks: List<DeepLink>) {
            check(imported == null) { "importAll called twice" }
            imported = folders to deepLinks
        }

        override fun getDeepLinkByLink(link: String): DeepLink? = null
        override fun getDeepLinksStream(): Flow<List<DeepLink>> = emptyFlow()
        override fun getDeepLinks(): List<DeepLink> = emptyList()
        override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> = emptyFlow()
        override fun getDeepLinkById(id: String): DeepLink? = null

        override suspend fun insert(deepLink: DeepLink) = unexpected()
        override suspend fun updateLink(id: String, link: String) = unexpected()
        override suspend fun updateName(id: String, name: String?) = unexpected()
        override suspend fun updateDescription(id: String, description: String?) = unexpected()
        override suspend fun updateTargetPackage(id: String, targetPackage: String?) = unexpected()
        override suspend fun setFavorite(id: String, isFavorite: Boolean) = unexpected()
        override suspend fun setFolder(id: String, folderId: String?) = unexpected()
        override suspend fun recordLaunch(id: String, launchedAt: LocalDateTime) = unexpected()
        override suspend fun delete(id: String) = unexpected()
        override suspend fun deleteAll(): List<String> = unexpected()

        private fun unexpected(): Nothing = error("import must only write through importAll")
    }
}
