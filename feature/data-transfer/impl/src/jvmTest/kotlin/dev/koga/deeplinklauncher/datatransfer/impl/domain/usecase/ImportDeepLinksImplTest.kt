package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.data.dto.Payload
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.model.FileType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class ImportDeepLinksImplTest {

    private val fixture = Fixture()

    @AfterTest
    fun tearDown() {
        fixture.close()
    }

    @Test
    fun mergesImportedFolderIntoLocalFolderWithSameName() = runTest {
        val work = Folder(id = "local", name = "Work", description = "Mine")
        fixture.folders.upsertFolder(work)
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "x", link = "myapp://x", folder = work))

        val result = fixture.importJson(
            Payload(
                folders = listOf(Payload.Folder(id = "remote", name = "Work", description = "Theirs")),
                deepLinks = listOf(Payload.DeepLink(link = "myapp://y", id = "y", folderId = "remote")),
            ),
        )

        assertEquals(ImportDeepLinks.Result.Success, result)
        assertEquals(listOf(work.copy(description = "Theirs")), fixture.folders.getFolders())
        assertEquals("local", fixture.deepLinks.getDeepLinkById("x")?.folder?.id)
        assertEquals("local", fixture.deepLinks.getDeepLinkById("y")?.folder?.id)
    }

    @Test
    fun keepsLocalFolderDescriptionWhenFileHasNone() = runTest {
        val work = Folder(id = "local", name = "Work", description = "Mine")
        fixture.folders.upsertFolder(work)

        fixture.importJson(Payload(folders = listOf(Payload.Folder(id = "remote", name = "Work")), deepLinks = emptyList()))

        assertEquals(listOf(work), fixture.folders.getFolders())
    }

    @Test
    fun renamesLocalFolderWhenFileHasSameId() = runTest {
        val a = Folder(id = "f", name = "A", description = null)
        fixture.folders.upsertFolder(a)
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "x", link = "myapp://x", folder = a))

        fixture.importJson(
            Payload(
                folders = listOf(Payload.Folder(id = "f", name = "X")),
                deepLinks = listOf(Payload.DeepLink(link = "myapp://x", id = "x", folderId = "f")),
            ),
        )

        assertEquals(listOf(Folder(id = "f", name = "X", description = null)), fixture.folders.getFolders())
        assertEquals("f", fixture.deepLinks.getDeepLinkById("x")?.folder?.id)
    }

    @Test
    fun mergesImportedLinkOntoExistingRowKeepingItsId() = runTest {
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "local-id", link = "myapp://x").copy(name = "Local"))

        fixture.importJson(
            Payload(deepLinks = listOf(Payload.DeepLink(link = "myapp://x", id = "file-id", name = "Imported"))),
        )

        assertEquals(listOf("local-id" to "Imported"), fixture.deepLinks.getDeepLinks().map { it.id to it.name })
    }

    @Test
    fun keepsLocalDeepLinkWhenImportedIdBelongsToAnotherLink() = runTest {
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "shared-id", link = "myapp://edited"))

        fixture.importJson(
            Payload(deepLinks = listOf(Payload.DeepLink(link = "myapp://original", id = "shared-id"))),
        )

        assertEquals("myapp://edited", fixture.deepLinks.getDeepLinkById("shared-id")?.link)
        val imported = assertNotNull(fixture.deepLinks.getDeepLinkByLink("myapp://original"))
        assertNotEquals("shared-id", imported.id)
        assertEquals(2, fixture.deepLinks.getDeepLinks().size)
    }

    @Test
    fun importsDuplicateLinkOnceLastEntryWins() = runTest {
        val result = fixture.importJson(
            Payload(
                deepLinks = listOf(
                    Payload.DeepLink(link = "myapp://dup", id = "first", name = "First"),
                    Payload.DeepLink(link = "myapp://dup", id = "last", name = "Last"),
                ),
            ),
        )

        assertEquals(ImportDeepLinks.Result.Success, result)
        assertEquals(listOf("last" to "Last"), fixture.deepLinks.getDeepLinks().map { it.id to it.name })
        assertEquals(listOf("last"), fixture.shortcuts.enabled)
    }

    @Test
    fun importsEachTextLinkOnce() = runTest {
        val result = fixture.importText("myapp://a\nmyapp://a\nmyapp://b")

        assertEquals(ImportDeepLinks.Result.Success, result)
        assertEquals(listOf("myapp://a", "myapp://b"), fixture.deepLinks.getDeepLinks().map { it.link })
    }

    @Test
    fun enablesShortcutsOnlyForCreatedIds() = runTest {
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "kept", link = "myapp://kept"))

        fixture.importJson(
            Payload(
                deepLinks = listOf(
                    Payload.DeepLink(link = "myapp://kept", id = "kept"),
                    Payload.DeepLink(link = "myapp://new", id = "new"),
                    Payload.DeepLink(link = "myapp://other", id = "kept"),
                ),
            ),
        )

        val otherId = assertNotNull(fixture.deepLinks.getDeepLinkByLink("myapp://other")).id
        assertNotEquals("kept", otherId)
        assertEquals(listOf("new", otherId), fixture.shortcuts.enabled)
    }

    @Test
    fun refreshesTheShortcutOfAnExistingLinkWithTheImportedFields() = runTest {
        fixture.deepLinks.upsertDeepLink(localDeepLink(id = "pinned", link = "myapp://x").copy(name = "Foo"))

        fixture.importJson(
            Payload(
                deepLinks = listOf(
                    Payload.DeepLink(link = "myapp://x", name = "Bar", description = "From backup", targetPackage = "com.b"),
                ),
            ),
        )

        val refreshed = fixture.shortcuts.updated.single()
        assertEquals("pinned", refreshed.id)
        assertEquals(Triple("Bar", "From backup", "com.b"), Triple(refreshed.name, refreshed.description, refreshed.targetPackage))
        assertEquals(emptyList(), fixture.shortcuts.enabled)
    }

    @Test
    fun refreshesARecreatedShortcutAfterEnablingIt() = runTest {
        fixture.importJson(
            Payload(deepLinks = listOf(Payload.DeepLink(link = "myapp://old", id = "x", name = "Restored"))),
        )

        assertEquals(listOf("enable:x", "update:x"), fixture.shortcuts.events)
        assertEquals("Restored", fixture.shortcuts.updated.single().name)
    }

    @Test
    fun importDoesNotRenameAFolderAlreadyMergedByName() = runTest {
        val fileFolders = listOf(
            Payload.Folder(id = "f", name = "B"),
            Payload.Folder(id = "g", name = "C"),
        )

        listOf(fileFolders, fileFolders.reversed()).forEach { orderedFolders ->
            Fixture().use { scenario ->
                val a = Folder(id = "f", name = "A", description = null)
                val b = Folder(id = "g", name = "B", description = null)
                scenario.folders.upsertFolder(a)
                scenario.folders.upsertFolder(b)
                scenario.deepLinks.upsertDeepLink(localDeepLink(id = "x", link = "myapp://x", folder = a))
                scenario.deepLinks.upsertDeepLink(localDeepLink(id = "y", link = "myapp://y", folder = b))

                scenario.importJson(
                    Payload(
                        folders = orderedFolders,
                        deepLinks = listOf(
                            Payload.DeepLink(link = "myapp://x", id = "x", folderId = "f"),
                            Payload.DeepLink(link = "myapp://y", id = "y", folderId = "g"),
                        ),
                    ),
                )

                val namesById = scenario.folders.getFolders().associate { it.id to it.name }
                val message = "file folders ${orderedFolders.map { it.id }}"
                assertEquals("A", namesById["f"], message)
                assertEquals("B", namesById["g"], message)
                assertEquals(setOf("A", "B", "C"), namesById.values.toSet(), message)
                assertEquals("B", namesById[scenario.deepLinks.getDeepLinkById("x")?.folder?.id], message)
                assertEquals("C", namesById[scenario.deepLinks.getDeepLinkById("y")?.folder?.id], message)
            }
        }
    }

    @Test
    fun duplicateNameGroupNeverRenamesALocalFolder() = runTest {
        val fileFolders = listOf(
            Payload.Folder(id = "f6", name = "C"),
            Payload.Folder(id = "f1", name = "C"),
        )

        listOf(fileFolders, fileFolders.reversed()).forEach { orderedFolders ->
            Fixture().use { scenario ->
                val d = Folder(id = "f1", name = "D", description = null)
                scenario.folders.upsertFolder(d)
                scenario.deepLinks.upsertDeepLink(localDeepLink(id = "z", link = "myapp://z", folder = d))

                scenario.importJson(
                    Payload(
                        folders = orderedFolders,
                        deepLinks = listOf(Payload.DeepLink(link = "myapp://x", id = "x", folderId = "f6")),
                    ),
                )

                val namesById = scenario.folders.getFolders().associate { it.id to it.name }
                val message = "file folders ${orderedFolders.map { it.id }}"
                assertEquals("D", namesById["f1"], message)
                assertEquals(setOf("C", "D"), namesById.values.toSet(), message)
                assertEquals("D", namesById[scenario.deepLinks.getDeepLinkById("z")?.folder?.id], message)
                assertEquals("C", namesById[scenario.deepLinks.getDeepLinkById("x")?.folder?.id], message)
            }
        }
    }

    private fun localDeepLink(id: String, link: String, folder: Folder? = null) = DeepLink(
        id = id,
        link = link,
        name = null,
        description = null,
        createdAt = LocalDateTime(2026, 1, 15, 10, 30),
        isFavorite = false,
        folder = folder,
    )

    private class Fixture : AutoCloseable {
        val folders = FakeFolderRepository()
        val deepLinks = FakeDeepLinkRepository()
        val shortcuts = FakeShortcutManager()
        private val file = File.createTempFile("deeplinks", ".import")
        private val importDeepLinks = ImportDeepLinksImpl(
            getFileContent = GetFileContent(),
            deepLinkRepository = deepLinks,
            folderRepository = folders,
            validateDeepLink = AcceptAllLinks,
            shortcutManager = shortcuts,
        )

        suspend fun importJson(payload: Payload): ImportDeepLinks.Result {
            file.writeText(Json.encodeToString(Payload.serializer(), payload))
            return importDeepLinks(filePath = file.absolutePath, fileType = FileType.JSON)
        }

        suspend fun importText(text: String): ImportDeepLinks.Result {
            file.writeText(text)
            return importDeepLinks(filePath = file.absolutePath, fileType = FileType.TXT)
        }

        override fun close() {
            file.delete()
        }
    }

    private object AcceptAllLinks : ValidateDeepLink {
        override fun isValid(link: String): Boolean = true
    }

    private class FakeFolderRepository : FolderRepository {
        private val folders = linkedMapOf<String, Folder>()

        override fun getFoldersStream(): Flow<List<Folder>> = flowOf(getFolders())

        override fun getFolders(): List<Folder> = folders.values.toList()

        override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> = throw UnsupportedOperationException()

        override fun getFolderByIdStream(id: String): Flow<Folder?> = flowOf(folders[id])

        override fun getFolderById(id: String): Folder? = folders[id]

        override fun upsertFolder(folder: Folder): FolderRepository.UpsertResult {
            val other = folders.values.firstOrNull { it.name == folder.name && it.id != folder.id }
            if (other != null) return FolderRepository.UpsertResult.NameAlreadyExists(other.id)

            folders[folder.id] = folder
            return FolderRepository.UpsertResult.Saved
        }

        override fun deleteFolder(id: String) {
            folders.remove(id)
        }

        override fun deleteAll() {
            folders.clear()
        }
    }

    private class FakeDeepLinkRepository : DeepLinkRepository {
        private val deepLinks = linkedMapOf<String, DeepLink>()

        override fun getDeepLinksStream(): Flow<List<DeepLink>> = flowOf(getDeepLinks())

        override fun getDeepLinks(): List<DeepLink> = deepLinks.values.toList()

        override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> = flowOf(deepLinks[id])

        override fun getDeepLinkById(id: String): DeepLink? = deepLinks[id]

        override fun getDeepLinkByLink(link: String): DeepLink? = deepLinks.values.firstOrNull { it.link == link }

        override fun upsertDeepLink(deepLink: DeepLink): DeepLinkRepository.UpsertResult {
            val other = deepLinks.values.firstOrNull { it.link == deepLink.link && it.id != deepLink.id }
            if (other != null) return DeepLinkRepository.UpsertResult.LinkAlreadyExists(other.id)

            deepLinks[deepLink.id] = deepLink
            return DeepLinkRepository.UpsertResult.Saved
        }

        override fun updateLastLaunchedAt(id: String, lastLaunchedAt: LocalDateTime) {
            deepLinks[id]?.let { deepLinks[id] = it.copy(lastLaunchedAt = lastLaunchedAt) }
        }

        override fun deleteDeepLink(id: String) {
            deepLinks.remove(id)
        }

        override fun deleteAll() {
            deepLinks.clear()
        }
    }

    private class FakeShortcutManager : DeepLinkShortcutManager {
        val enabled = mutableListOf<String>()
        val events = mutableListOf<String>()
        val updated = mutableListOf<DeepLink>()

        override suspend fun isAdded(deepLinkId: String): Boolean = false

        override suspend fun add(deepLink: DeepLink): DeepLinkShortcutManager.AddResult =
            DeepLinkShortcutManager.AddResult.NotSupported

        override suspend fun update(deepLink: DeepLink) {
            updated += deepLink
            events += "update:${deepLink.id}"
        }

        override suspend fun remove(deepLinkId: String) = Unit

        override suspend fun enable(deepLinkIds: List<String>) {
            enabled += deepLinkIds
            events += deepLinkIds.map { "enable:$it" }
        }

        override suspend fun disable(deepLinkIds: List<String>) = Unit
    }
}
