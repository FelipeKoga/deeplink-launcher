package dev.koga.deeplinklauncher.impl.util.ext

import dev.koga.deeplinklauncher.devicebridge.impl.ext.installed
import junit.framework.TestCase
import org.junit.Test

class InstalledTest {

    @Test
    fun `should return true if the program is installed`() {
        TestCase.assertTrue("java".installed())
    }

    @Test
    fun `should return false if the program isn't installed`() {
        TestCase.assertFalse("unknown-program".installed())
    }
}
