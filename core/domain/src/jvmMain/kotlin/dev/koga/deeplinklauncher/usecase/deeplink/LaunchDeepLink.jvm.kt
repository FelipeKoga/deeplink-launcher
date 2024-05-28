package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime

actual class LaunchDeepLink(
    private val dataSource: DeepLinkDataSource,
) {
    actual fun launch(url: String): LaunchDeepLinkResult {
        return try {
            val command = "adb shell am start -a android.intent.action.VIEW -d \"$url\""
            val process = Runtime.getRuntime().exec(command)
            process.waitFor() // Wait for the command to complete

            if (process.exitValue() == 0) {
                LaunchDeepLinkResult.Success(url)
            } else {
                val errorStream = process.errorStream.bufferedReader().readText()
                LaunchDeepLinkResult.Failure(Exception("ADB command failed with error: $errorStream"))
            }
        } catch (e: Throwable) {
            LaunchDeepLinkResult.Failure(e)
        }
    }

    actual fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        dataSource.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
