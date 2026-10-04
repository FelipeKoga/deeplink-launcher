package dev.koga.deeplinklauncher.cli

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.parse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExitCodeTest {

    @Test
    fun usageErrorsExitWithTwo() {
        val error = assertFailsWith<CliktError> { deeplinkCli { Toolchain(adb = null, simctl = null) }.parse(listOf("open", "--bogus")) }

        assertEquals(ExitCode.USAGE.code, exitCodeOf(error))
    }

    @Test
    fun helpExitsWithZero() {
        val error = assertFailsWith<CliktError> { deeplinkCli().parse(listOf("open", "--help")) }

        assertEquals(ExitCode.OK.code, exitCodeOf(error))
    }
}
