package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import java.awt.Desktop
import java.net.URI

actual class LaunchDeepLink(
    private val dataSource: DeepLinkDataSource,
    private val startActivity: StartActivity,
) {
    actual fun launch(url: String): LaunchDeepLinkResult {
        val adbResult = launchWithAdb(url)

        if (adbResult is LaunchDeepLinkResult.Success) {
            return adbResult
        }

        val browserResult = launchDesktopBrowser(url)

        return browserResult
    }

    private fun launchWithAdb(link: String): LaunchDeepLinkResult {
        return try {
            val process = startActivity(
                action = "android.intent.action.VIEW",
                arg = link,
            )

            return if (process.exitValue() == 0) {
                LaunchDeepLinkResult.Success(link)
            } else {
                val errorStream = process.errorStream.bufferedReader().readText()
                LaunchDeepLinkResult.Failure(Exception("ADB command failed with error: $errorStream"))
            }
        } catch (e: Exception) {
            LaunchDeepLinkResult.Failure(e)
        }
    }

    private fun launchDesktopBrowser(link: String): LaunchDeepLinkResult {
        return try {
            if (!Desktop.isDesktopSupported()) {
                throw UnsupportedOperationException("Desktop is not supported on this platform.")
            }

            val desktop = Desktop.getDesktop()
            val uri = URI(link)
            desktop.browse(uri)

            LaunchDeepLinkResult.Success(link)
        } catch (e: Exception) {
            LaunchDeepLinkResult.Failure(e)
        }
    }

    actual fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        dataSource.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
