package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkTarget
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import java.awt.Desktop
import java.net.URI

internal class LaunchDeepLinkImpl(
    private val repository: DeepLinkRepository,
    private val deviceBridge: DeviceBridge,
    private val deepLinkTargetStateManager: DeepLinkTargetStateManager,
) : LaunchDeepLink {
    override suspend fun launch(url: String): LaunchDeepLink.Result {
        return when (val target = deepLinkTargetStateManager.current.value) {
            is DeepLinkTarget.Desktop -> launchDesktop(url)
            is DeepLinkTarget.Device -> launch(url, target)
        }
    }

    private suspend fun launch(
        link: String,
        device: DeepLinkTarget.Device,
    ): LaunchDeepLink.Result {
        return try {
            val process = deviceBridge.launch(
                id = device.id,
                link = link,
            )

            return if (process.exitValue() == 0) {
                LaunchDeepLink.Result.Success(link)
            } else {
                val errorStream = process.errorStream.bufferedReader().readText()
                LaunchDeepLink.Result.Failure(Exception("Command failed with error: $errorStream"))
            }
        } catch (e: Exception) {
            LaunchDeepLink.Result.Failure(e)
        }
    }

    private fun launchDesktop(link: String): LaunchDeepLink.Result {
        return try {
            if (!Desktop.isDesktopSupported()) {
                throw UnsupportedOperationException("Desktop is not supported on this platform.")
            }

            val desktop = Desktop.getDesktop()
            val uri = URI(link)
            desktop.browse(uri)

            LaunchDeepLink.Result.Success(link)
        } catch (e: Exception) {
            LaunchDeepLink.Result.Failure(e)
        }
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result {
        repository.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
