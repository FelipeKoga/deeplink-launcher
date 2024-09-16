package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.DeeplinkTargetStateManager
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.DeeplinkTarget
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import java.awt.Desktop
import java.net.URI

actual class LaunchDeepLink(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val deviceBridge: DeviceBridge,
    private val deeplinkTargetStateManager: DeeplinkTargetStateManager,
) {
    actual suspend fun launch(url: String): LaunchDeepLinkResult {
        return when (val target = deeplinkTargetStateManager.current.value) {
            is DeeplinkTarget.Browser -> launchDesktopBrowser(url)
            is DeeplinkTarget.Device -> launch(url, target)
        }
    }

    private suspend fun launch(
        link: String,
        device: DeeplinkTarget.Device,
    ): LaunchDeepLinkResult {
        return try {
            val process = deviceBridge.launch(
                id = device.id,
                link = link,
            )

            return if (process.exitValue() == 0) {
                LaunchDeepLinkResult.Success(link)
            } else {
                val errorStream = process.errorStream.bufferedReader().readText()
                LaunchDeepLinkResult.Failure(Exception("Command failed with error: $errorStream"))
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
