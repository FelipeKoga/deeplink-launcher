package dev.koga.deeplinklauncher.datatransfer.api.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

public sealed interface DataTransferRoute : AppRoute {

    @Serializable
    public data object ImportData : DataTransferRoute {
        @Transient
        override val analyticsScreenName: String = "import_data"
    }

    @Serializable
    public data object ExportData : DataTransferRoute {
        @Transient
        override val analyticsScreenName: String = "export_data"
    }
}
