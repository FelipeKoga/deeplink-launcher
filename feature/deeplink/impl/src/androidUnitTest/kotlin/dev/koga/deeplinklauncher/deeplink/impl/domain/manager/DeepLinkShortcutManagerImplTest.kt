package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import android.content.Context
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class DeepLinkShortcutManagerImplTest {

    private lateinit var context: Context
    private lateinit var manager: DeepLinkShortcutManagerImpl

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        manager = DeepLinkShortcutManagerImpl(context)
    }

    @Test
    fun addsAValidDeepLink() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Added, manager.add(deepLink(id = "id", link = "myapp://home")))
        assertTrue(manager.isAdded("id"))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyId() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Failed, manager.add(deepLink(id = "", link = "myapp://home")))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyLinkWithoutName() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Failed, manager.add(deepLink(id = "id", link = "")))
        assertFalse(manager.isAdded("id"))
    }

    @Test
    fun ignoresEmptyIdsAndKeepsHandlingTheOthers() = runBlocking {
        manager.add(deepLink(id = "id", link = "myapp://home"))

        manager.enable(listOf("", "id"))
        manager.disable(listOf("", "id"))
        manager.update(deepLink(id = "", link = ""))
        manager.update(deepLink(id = "id", link = ""))
        manager.remove("")
        manager.remove("id")

        assertFalse(manager.isAdded("id"))
    }

    private fun deepLink(id: String, link: String) = DeepLink(
        id = id,
        link = link,
        name = null,
        description = null,
        isFavorite = false,
    )
}
