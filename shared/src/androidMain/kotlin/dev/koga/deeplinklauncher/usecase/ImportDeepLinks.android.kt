package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

actual class ImportDeepLinks(
    private val deepLinkRepository: DeepLinkRepository
) {
    actual fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput {
        return try {
            val fileContents = File(filePath).readText()

            when (fileType) {
                FileType.JSON -> {
                    val parsedData = Json.decodeFromString<List<ImportDeepLinkDto>>(fileContents)
                    deepLinkRepository.upsertAll(parsedData.map {
                        DeepLink(
                            id = it.id ?: UUID.randomUUID().toString(),
                            createdAt = Clock.System.now(),
                            link = it.link,
                            name = it.name,
                            description = it.description,
                            isFavorite = it.isFavorite ?: false,
                            folder = it.folder?.let { folder ->
                                Folder(
                                    id = folder.id ?: UUID.randomUUID().toString(),
                                    name = folder.name,
                                    description = folder.description,
                                    deepLinkCount = 0
                                )
                            }
                        )
                    })
                }

                FileType.TXT -> {
                    val deepLinks = fileContents.split("\n")

                    deepLinks.forEach {
                        val parsedData = DeepLink(
                            id = UUID.randomUUID().toString(),
                            link = it,
                            name = "",
                            description = "",
                            createdAt = Clock.System.now(),
                            isFavorite = false,
                            folder = null
                        )

                        deepLinkRepository.upsert(parsedData)
                    }
                }
            }

            ImportDeepLinksOutput.Success
        } catch (e: Exception) {
            ImportDeepLinksOutput.Error(e)
        }
    }
}