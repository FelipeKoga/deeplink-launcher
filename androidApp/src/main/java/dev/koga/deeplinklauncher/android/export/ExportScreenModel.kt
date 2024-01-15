package dev.koga.deeplinklauncher.android.export

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.FileType
import kotlinx.coroutines.launch

class ExportScreenModel(
    private val exportDeepLinks: ExportDeepLinks
) : ScreenModel {

    fun export(type: FileType) {
        screenModelScope.launch {
            exportDeepLinks.export(type)
        }
    }
}