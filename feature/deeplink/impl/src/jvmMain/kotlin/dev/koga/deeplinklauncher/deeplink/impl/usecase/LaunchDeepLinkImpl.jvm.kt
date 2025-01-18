package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.devicebridge.DeeplinkTargetStateManager
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.model.DeeplinkTarget
import java.awt.Desktop
import java.net.URI

actual class LaunchDeepLinkImpl(
    private val repository: DeepLinkRepository,
    private val deviceBridge: DeviceBridge,
    private val deeplinkTargetStateManager: DeeplinkTargetStateManager,
) : LaunchDeepLink {
    actual override suspend fun launch(url: String): LaunchDeepLink.Result {
        return when (val target = deeplinkTargetStateManager.current.value) {
            is DeeplinkTarget.Desktop -> launchDesktop(url)
            is DeeplinkTarget.Device -> launch(url, target)
        }
    }

    private suspend fun launch(
        link: String,
        device: DeeplinkTarget.Device,
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

    actual override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result {
        repository.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
