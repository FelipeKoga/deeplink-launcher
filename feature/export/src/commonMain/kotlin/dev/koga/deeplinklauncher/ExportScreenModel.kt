package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksPlainTextPreview

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
