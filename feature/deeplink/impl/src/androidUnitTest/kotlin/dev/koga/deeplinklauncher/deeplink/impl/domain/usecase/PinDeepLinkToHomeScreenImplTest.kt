package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.content.pm.ShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class PinDeepLinkToHomeScreenImplTest {

    private lateinit var context: Context
    private lateinit var pin: PinDeepLinkToHomeScreenImpl

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        shadowOf(context.getSystemService(ShortcutManager::class.java)).setIsRequestPinShortcutSupported(true)
        pin = PinDeepLinkToHomeScreenImpl(context)
    }

    @Test
    fun requestsThePinForAValidDeepLink() {
        assertEquals(PinDeepLinkToHomeScreen.Result.Requested, pin(deepLink(id = "id", link = "myapp://home")))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyLinkWithoutName() {
        assertEquals(PinDeepLinkToHomeScreen.Result.Failed, pin(deepLink(id = "id", link = "")))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyId() {
        assertEquals(PinDeepLinkToHomeScreen.Result.Failed, pin(deepLink(id = "", link = "myapp://home")))
    }

    private fun deepLink(id: String, link: String) = DeepLink(
        id = id,
        link = link,
        name = null,
        description = null,
        isFavorite = false,
    )
}
