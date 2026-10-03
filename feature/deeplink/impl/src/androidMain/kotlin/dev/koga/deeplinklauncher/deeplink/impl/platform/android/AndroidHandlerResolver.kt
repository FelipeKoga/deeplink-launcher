package dev.koga.deeplinklauncher.deeplink.impl.platform.android

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.roundToInt

@SingleIn(AppScope::class)
@Inject
internal class AndroidHandlerResolver(context: Context) {

    internal class ResolvedHandler(
        val packageName: String,
        val label: String,
        val icon: DeepLinkIcon,
        val resolvePackageName: String?,
    )

    private sealed interface CachedResolution {
        class Found(val handler: ResolvedHandler) : CachedResolution
        data object NotFound : CachedResolution
    }

    private val appContext = context.applicationContext
    private val packageManager = appContext.packageManager

    private val byLink = LruCache<String, CachedResolution>(LINK_CACHE_SIZE)
    private val unresolvableLinks = LruCache<String, Unit>(LINK_CACHE_SIZE)

    private val byComponent = ConcurrentHashMap<String, ResolvedHandler>()
    private val buildLocks = ConcurrentHashMap<String, Any>()

    private val linkGeneration = AtomicInteger()
    private val componentGeneration = AtomicInteger()
    private val nextIconId = AtomicLong()
    private val lastPackageCheckMs = AtomicLong()

    @Volatile
    private var packageSequenceNumber = 0

    init {
        registerForegroundEviction()
        appContext.registerComponentCallbacks(ConfigurationEviction())
    }

    fun resolve(link: String, targetPackage: String?): ResolvedHandler? {
        invalidateIfPackagesChanged()

        val key = handlerCacheKey(link, targetPackage)
        when (val cached = byLink.get(key)) {
            is CachedResolution.Found -> return cached.handler
            CachedResolution.NotFound -> return null
            null -> Unit
        }
        if (unresolvableLinks.get(key) != null) return null

        val resolveLinkGeneration = linkGeneration.get()
        val resolveComponentGeneration = componentGeneration.get()
        val intent = createDeepLinkViewIntent(link, targetPackage)
        val handler = runCatching {
            packageManager.resolveActivity(intent, 0)
                ?.let { internHandler(it, resolveComponentGeneration) }
        }.getOrNull()

        when {
            handler != null -> byLink.put(key, CachedResolution.Found(handler))
            intent.isWebIntent() -> byLink.put(key, CachedResolution.NotFound)
            else -> unresolvableLinks.put(key, Unit)
        }
        if (linkGeneration.get() != resolveLinkGeneration) {
            byLink.remove(key)
            unresolvableLinks.remove(key)
        }
        return handler
    }

    private fun handlerCacheKey(link: String, targetPackage: String?): String =
        "${link.trim()}|${targetPackage.orEmpty()}"

    private fun evictResolutions() {
        linkGeneration.incrementAndGet()
        byLink.evictAll()
    }

    private fun evictAll() {
        linkGeneration.incrementAndGet()
        componentGeneration.incrementAndGet()
        byLink.evictAll()
        unresolvableLinks.evictAll()
        byComponent.clear()
    }

    private fun evictPackages(packages: Collection<String>) {
        linkGeneration.incrementAndGet()
        componentGeneration.incrementAndGet()
        byLink.evictAll()
        unresolvableLinks.evictAll()
        byComponent.values.removeAll { handler ->
            handler.packageName in packages || handler.resolvePackageName in packages
        }
    }

    private fun internHandler(
        resolveInfo: ResolveInfo,
        resolveComponentGeneration: Int,
    ): ResolvedHandler {
        val activityInfo = resolveInfo.activityInfo
        val componentKey = "${activityInfo.packageName}/${activityInfo.name}" +
            "|${resolveInfo.icon}|${resolveInfo.labelRes}" +
            "|${resolveInfo.nonLocalizedLabel}|${resolveInfo.resolvePackageName}"
        byComponent[componentKey]?.let { return it }

        val lock = buildLocks.computeIfAbsent(componentKey) { Any() }
        return synchronized(lock) {
            byComponent[componentKey] ?: createHandler(resolveInfo).also { built ->
                byComponent[componentKey] = built
                if (componentGeneration.get() != resolveComponentGeneration) {
                    byComponent.remove(componentKey, built)
                }
            }
        }
    }

    private fun createHandler(resolveInfo: ResolveInfo): ResolvedHandler {
        val iconMaxPx =
            (HANDLER_ICON_MAX_DP * appContext.resources.displayMetrics.density).roundToInt()

        return ResolvedHandler(
            packageName = resolveInfo.activityInfo.packageName,
            label = resolveInfo.loadLabel(packageManager).toString(),
            icon = DeepLinkIcon(
                id = nextIconId.incrementAndGet(),
                byteArray = resolveInfo.loadIcon(packageManager)
                    .toCappedBitmap(iconMaxPx)
                    .toPngByteArray(),
            ),
            resolvePackageName = resolveInfo.resolvePackageName,
        )
    }

    private fun invalidateIfPackagesChanged() {
        val now = SystemClock.elapsedRealtime()
        val last = lastPackageCheckMs.get()
        if (now - last < PACKAGE_CHECK_INTERVAL_MS) return
        if (!lastPackageCheckMs.compareAndSet(last, now)) return

        val changed = packageManager.getChangedPackages(packageSequenceNumber) ?: return
        packageSequenceNumber = changed.sequenceNumber
        evictPackages(changed.packageNames)
    }

    private fun registerForegroundEviction() {
        val application = appContext as? Application ?: return
        application.registerActivityLifecycleCallbacks(ForegroundEvictionCallbacks())
    }

    private inner class ForegroundEvictionCallbacks : Application.ActivityLifecycleCallbacks {
        private var stoppedSinceLastResume = false

        override fun onActivityStopped(activity: Activity) {
            stoppedSinceLastResume = true
        }

        override fun onActivityResumed(activity: Activity) {
            if (stoppedSinceLastResume) {
                stoppedSinceLastResume = false
                evictResolutions()
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityDestroyed(activity: Activity) = Unit
    }

    private inner class ConfigurationEviction : ComponentCallbacks {
        private var locales = appContext.resources.configuration.locales
        private var densityDpi = appContext.resources.configuration.densityDpi

        override fun onConfigurationChanged(newConfig: Configuration) {
            if (newConfig.locales == locales && newConfig.densityDpi == densityDpi) return
            locales = newConfig.locales
            densityDpi = newConfig.densityDpi
            evictAll()
        }

        @Deprecated("Deprecated in Java")
        override fun onLowMemory() = Unit
    }

    private companion object {
        const val LINK_CACHE_SIZE = 4096
        const val HANDLER_ICON_MAX_DP = 48f
        const val PACKAGE_CHECK_INTERVAL_MS = 1_000L
    }
}

private fun Intent.isWebIntent(): Boolean =
    scheme.equals("http", ignoreCase = true) || scheme.equals("https", ignoreCase = true)

private fun Drawable.toCappedBitmap(maxPx: Int): Bitmap {
    val width = intrinsicWidth
    val height = intrinsicHeight
    if (width <= 0 || height <= 0) return toBitmap(width = maxPx, height = maxPx)

    val scale = minOf(1f, maxPx.toFloat() / maxOf(width, height))
    return toBitmap(
        width = (width * scale).roundToInt().coerceAtLeast(1),
        height = (height * scale).roundToInt().coerceAtLeast(1),
    )
}
