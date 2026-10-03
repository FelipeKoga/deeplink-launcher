package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.createGraph
import kotlin.test.Test
import kotlin.test.assertNotSame

class DesktopAppGraphTest {
    @Test
    fun createsAGraphPerCall() {
        assertNotSame(createGraph<DesktopAppGraph>(), createGraph<DesktopAppGraph>())
    }
}
