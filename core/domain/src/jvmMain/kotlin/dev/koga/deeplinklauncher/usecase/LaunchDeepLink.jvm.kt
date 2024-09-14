package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.TargetDataSource
import dev.koga.deeplinklauncher.manager.AdbManager
import dev.koga.deeplinklauncher.manager.XcrunManager
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Target
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import kotlinx.coroutines.runBlocking
import java.awt.Desktop
import java.net.URI

actual class LaunchDeepLink(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val targetDataSource: TargetDataSource,
    private val adbManager: AdbManager,
    private val xcrunManager: XcrunManager,
) {
    actual suspend fun launch(url: String): LaunchDeepLinkResult {
        return when (val target = targetDataSource.current.value) {
            is Target.Browser -> launchDesktopBrowser(url)
            is Target.Device -> launch(url, target)
        }
    }

    private suspend fun launch(
        link: String,
        target: Target.Device
    ): LaunchDeepLinkResult {
        return try {
            val process = when (target.platform) {
                Platform.ANDROID -> adbManager.execute(
                    serial = target.serial,
                    action = "android.intent.action.VIEW",
                    arg = link,
                )

                Platform.IOS -> xcrunManager.execute(
                    udid = target.serial,
                    arg = link
                )

                Platform.JVM -> TODO()
            }

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

    actual suspend fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        deepLinkDataSource.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
