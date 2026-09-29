package dev.koga.deeplinklauncher.impl.devicebridge.xcrun

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.xcrun.Xcrun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Test

class XcrunTest {

    @Test
    fun `should stop tracking when simctl fails`() = runBlocking {
        val xcrun = Xcrun(path = "/usr/bin/false", dispatcher = Dispatchers.IO)

        val emissions = withTimeout(10_000) { xcrun.track().toList() }

        assertEquals(listOf(emptyList<DeviceBridge.Device>(), emptyList()), emissions)
    }
}
