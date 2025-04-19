package dev.koga.deeplinklauncher.purchase.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import dev.koga.deeplinklauncher.navigation.FeatureNavigationApi
import dev.koga.deeplinklauncher.navigation.Route
import kotlinx.serialization.Serializable

interface PurchaseNavigation : FeatureNavigationApi {
    @Serializable
    data object PurchaseBottomSheet : Route

    override val rootRoute: Route
        get() = PurchaseBottomSheet
}

class PurchaseUIImpl : PurchaseNavigation {

    override fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        TODO("Not yet implemented")
    }

}