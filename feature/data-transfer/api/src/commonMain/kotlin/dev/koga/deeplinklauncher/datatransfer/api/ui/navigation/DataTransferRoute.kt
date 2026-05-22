package dev.koga.deeplinklauncher.datatransfer.api.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

public sealed interface DataTransferRoute : AppRoute {

    @Serializable
    public data object ImportData : DataTransferRoute

    @Serializable
    public data object ExportData : DataTransferRoute
}
