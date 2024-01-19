package dev.koga.deeplinklauncher.android.export

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExportScreenModel(
    getDeepLinksPlainTextPreview: GetDeepLinksPlainTextPreview,
    getDeepLinksJsonPreview: GetDeepLinksJsonPreview,
) : ScreenModel {


    private val plainTextPreview = getDeepLinksPlainTextPreview()
    private val jsonPreview = getDeepLinksJsonPreview()

    val preview = ExportData(
        plainTextFormat = plainTextPreview,
        jsonFormat = jsonPreview,
    )
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
)