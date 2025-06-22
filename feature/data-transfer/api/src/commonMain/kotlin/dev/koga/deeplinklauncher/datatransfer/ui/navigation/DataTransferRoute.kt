package dev.koga.deeplinklauncher.datatransfer.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

sealed interface DataTransferRoute : AppRoute {

    @Serializable
    data object ImportData : DataTransferRoute

    @Serializable
    data object ExportData : DataTransferRoute
}
