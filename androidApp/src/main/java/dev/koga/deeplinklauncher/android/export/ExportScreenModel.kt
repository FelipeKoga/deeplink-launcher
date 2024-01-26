package dev.koga.deeplinklauncher.android.export

import cafe.adriel.voyager.core.model.ScreenModel
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview

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