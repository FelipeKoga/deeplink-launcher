package dev.koga.deeplinklauncher.cli

import dev.koga.deeplinklauncher.cli.android.Component
import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.ios.Simctl
import dev.koga.deeplinklauncher.cli.link.ParsedLink
import dev.koga.deeplinklauncher.cli.output.Handler
import dev.koga.deeplinklauncher.cli.output.OpenReport
import dev.koga.deeplinklauncher.cli.output.OpenStatus
import dev.koga.deeplinklauncher.cli.output.ResolveReport
import dev.koga.deeplinklauncher.cli.output.ResolveStatus

internal class LinkActions(private val toolchain: Toolchain) {

    fun resolve(device: Device, url: String): ResolveReport = when (device.platform) {
        Platform.ANDROID -> resolveOnAndroid(device, url)
        Platform.IOS -> resolveOnIos(device, url)
    }

    fun open(device: Device, url: String, watchMs: Long): OpenReport = when (device.platform) {
        Platform.ANDROID -> openOnAndroid(device, url, watchMs)
        Platform.IOS -> openOnIos(device, url, watchMs)
    }

    private fun resolveOnAndroid(device: Device, url: String): ResolveReport {
        val adb = toolchain.requireAdb()
        val handlers = adb.handlers(device.id, url)
        val default = adb.defaultHandler(device.id, url)
        return ResolveReport(
            url = url,
            device = device,
            status = if (handlers.isEmpty()) ResolveStatus.UNHANDLED else ResolveStatus.HANDLED,
            defaultHandler = default?.takeUnless(Component::isChooser)?.toHandler(),
            handlers = handlers.map { it.toHandler() },
            note = if (default?.isChooser == true) "No default app: Android will ask the user to choose." else null,
        )
    }

    private fun resolveOnIos(device: Device, url: String): ResolveReport {
        val scheme = ParsedLink(url).scheme?.lowercase()
        if (scheme == null || scheme in WEB_SCHEMES) {
            return ResolveReport(
                url = url,
                device = device,
                status = ResolveStatus.UNKNOWN,
                defaultHandler = null,
                handlers = emptyList(),
                note = WEB_LINK_NOTE,
            )
        }
        val apps = toolchain.requireSimctl().handlers(device.id, scheme)
        val handlers = apps.map { Handler(id = it.bundleId, name = it.name) }
        return ResolveReport(
            url = url,
            device = device,
            status = if (handlers.isEmpty()) ResolveStatus.UNHANDLED else ResolveStatus.HANDLED,
            defaultHandler = handlers.singleOrNull(),
            handlers = handlers,
            note = if (handlers.size > 1) "Several apps declare the $scheme scheme; iOS picks one of them." else null,
        )
    }

    private fun openOnAndroid(device: Device, url: String, watchMs: Long): OpenReport {
        val adb = toolchain.requireAdb()
        val since = adb.deviceTime(device.id)
        val start = adb.start(device.id, url)
        val activity = start.activity
        if (activity == null || start.error != null) {
            return OpenReport(
                url = url,
                device = device,
                status = OpenStatus.UNHANDLED,
                handler = null,
                watchMs = watchMs,
                note = if (start.error != null) UNRESOLVED_ANDROID_NOTE else "No activity was started.",
            )
        }

        toolchain.sleep(watchMs)
        val alive = adb.isRunning(device.id, activity.packageName)
        return OpenReport(
            url = url,
            device = device,
            status = if (alive) OpenStatus.OPENED else OpenStatus.CRASHED,
            handler = activity.toHandler(),
            launch = start.launchState?.lowercase(),
            timeMs = start.totalTimeMs,
            alive = alive,
            watchMs = watchMs,
            crash = if (alive) null else adb.crashLog(device.id, activity.packageName, since),
            note = start.warning,
        )
    }

    private fun openOnIos(device: Device, url: String, watchMs: Long): OpenReport {
        val simctl = toolchain.requireSimctl()
        val resolution = resolveOnIos(device, url)
        val handler = resolution.defaultHandler
        val result = simctl.open(device.id, url)
        if (!result.succeeded) {
            return OpenReport(
                url = url,
                device = device,
                status = OpenStatus.UNHANDLED,
                handler = null,
                watchMs = watchMs,
                note = if (result.exitCode == Simctl.UNHANDLED_URL_EXIT_CODE) {
                    "No installed app handles this link."
                } else {
                    result.stderr.lineSequence().firstOrNull { it.isNotBlank() }?.trim()
                },
            )
        }

        if (handler == null) {
            return OpenReport(
                url = url,
                device = device,
                status = OpenStatus.OPENED,
                handler = null,
                watchMs = watchMs,
                note = resolution.note ?: "The app that opened the link could not be identified.",
            )
        }

        toolchain.sleep(watchMs)
        val alive = handler.id in simctl.runningBundleIds(device.id)
        return OpenReport(
            url = url,
            device = device,
            status = if (alive) OpenStatus.OPENED else OpenStatus.CRASHED,
            handler = handler,
            alive = alive,
            watchMs = watchMs,
            note = if (alive) {
                null
            } else {
                "${handler.id} is not running after $watchMs ms. Check ~/Library/Logs/DiagnosticReports."
            },
        )
    }

    private fun Component.toHandler() = Handler(id = flattened)

    private companion object {
        val WEB_SCHEMES = setOf("http", "https")
        const val UNRESOLVED_ANDROID_NOTE = "No app accepts this link from a browser " +
            "(no activity has a matching VIEW + BROWSABLE intent filter)."
        const val WEB_LINK_NOTE = "Web links open in Safari unless an app claims the domain with Universal Links, " +
            "which cannot be checked from the host."
    }
}
