package dev.koga.deeplinklauncher.platform

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class JvmAppDataDirectoryTest {

    private lateinit var tempDir: File

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("jvm-app-data-test").toFile()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun resolveCreatesDirectoryOnCurrentOs() {
        val dir = JvmAppDataDirectory.resolve()

        assertTrue(dir.exists())
        assertTrue(dir.isDirectory)
    }

    @Test
    fun resolvesMacOsPath() {
        val home = tempDir.absolutePath

        val dir = resolveAppDataDirectory(
            userHome = home,
            osName = "Mac OS X",
            appDataEnv = null,
            xdgDataHome = null,
        )

        assertEquals(
            File(home, "Library/Application Support/DeepLink Launcher"),
            dir,
        )
        assertTrue(dir.exists())
    }

    @Test
    fun resolvesWindowsPath() {
        val appData = tempDir.resolve("AppData/Roaming").apply { mkdirs() }.absolutePath

        val dir = resolveAppDataDirectory(
            userHome = tempDir.absolutePath,
            osName = "Windows 11",
            appDataEnv = appData,
            xdgDataHome = null,
        )

        assertEquals(File(appData, "DeepLink Launcher"), dir)
        assertTrue(dir.exists())
    }

    @Test
    fun resolvesWindowsPathWithoutAppData() {
        val home = tempDir.absolutePath

        val dir = resolveAppDataDirectory(
            userHome = home,
            osName = "Windows 10",
            appDataEnv = null,
            xdgDataHome = null,
        )

        assertEquals(File(home, "DeepLink Launcher"), dir)
        assertTrue(dir.exists())
    }

    @Test
    fun resolvesLinuxPath() {
        val home = tempDir.absolutePath

        val dir = resolveAppDataDirectory(
            userHome = home,
            osName = "Linux",
            appDataEnv = null,
            xdgDataHome = null,
        )

        assertEquals(File(home, ".local/share/deeplink-launcher"), dir)
        assertTrue(dir.exists())
    }

    @Test
    fun resolvesLinuxPathWithXdgDataHome() {
        val xdgDataHome = tempDir.resolve("xdg-data").apply { mkdirs() }.absolutePath

        val dir = resolveAppDataDirectory(
            userHome = tempDir.absolutePath,
            osName = "Linux",
            appDataEnv = null,
            xdgDataHome = xdgDataHome,
        )

        assertEquals(File(xdgDataHome, "deeplink-launcher"), dir)
        assertTrue(dir.exists())
    }
}
