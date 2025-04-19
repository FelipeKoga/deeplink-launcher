package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

interface Route

interface FeatureNavigationApi {

    @Serializable
    val rootRoute: Route

    fun registerGraph(
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
    )
}