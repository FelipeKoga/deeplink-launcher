package dev.koga.deeplinklauncher.android.export

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExportScreenModel(
    private val deepLinkRepository: DeepLinkRepository
) : ScreenModel {


    val formattedExportData = deepLinkRepository
        .getAllDeepLinks()
        .map {
            if (it.isEmpty()) {
                return@map ExportData(
                    plainTextFormat = "No deeplinks to export",
                    jsonFormat = "No deeplinks to export",
                    isEmpty = true,
                )
            }

            ExportData(
                plainTextFormat = it.joinToString(separator = "\n") { deepLink ->
                    deepLink.link
                },
                jsonFormat = it.joinToString(separator = "\n") { deepLink ->
                    """
                    {
                        "id": "${deepLink.id}",
                        "link": "${deepLink.link}",
                        "name": "${deepLink.name}",
                        "description": "${deepLink.description}",
                        "isFavorite": ${deepLink.isFavorite},
                        "folder": ${
                        deepLink.folder?.let { folder ->
                            """
                            {
                                "id": "${folder.id}",
                                "name": "${folder.name}",
                                "description": "${folder.description}"
                            }
                            """.trimIndent()
                        }
                    }
                    }
                    """.trimIndent()
                },
                isEmpty = false,
            )
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ExportData(
                plainTextFormat = "",
                jsonFormat = "",
                isEmpty = false,
            )
        )
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
    val isEmpty: Boolean,
)