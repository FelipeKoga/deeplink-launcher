package dev.koga.deeplinklauncher.platform

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class JvmStorageMigrationTest {

    private lateinit var tempDir: File

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("jvm-storage-migration-test").toFile()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun migratesLegacyFileToTarget() {
        val legacy = File(tempDir, "dll-db.db").apply { writeText("legacy-data") }
        val target = File(tempDir, "app/dll-db.db")

        migrateFileIfNeeded(legacyFile = legacy, targetFile = target)

        assertTrue(target.exists())
        assertFalse(legacy.exists())
        assertEquals("legacy-data", target.readText())
    }

    @Test
    fun skipsMigrationWhenTargetExists() {
        val legacy = File(tempDir, "legacy.db").apply { writeText("legacy") }
        val target = File(tempDir, "target.db").apply { writeText("existing") }

        migrateFileIfNeeded(legacyFile = legacy, targetFile = target)

        assertEquals("existing", target.readText())
        assertTrue(legacy.exists())
    }

    @Test
    fun skipsMigrationWhenLegacyDoesNotExist() {
        val target = File(tempDir, "target.db")

        migrateFileIfNeeded(
            legacyFile = File(tempDir, "missing.db"),
            targetFile = target,
        )

        assertFalse(target.exists())
    }

    @Test
    fun fallsBackToCopyWhenRenameFails() {
        val legacy = File(tempDir, "legacy.db").apply { writeText("legacy-data") }
        val target = File(tempDir, "app/target.db")

        migrateFileIfNeeded(
            legacyFile = legacy,
            targetFile = target,
            rename = { _, _ -> false },
        )

        assertTrue(target.exists())
        assertFalse(legacy.exists())
        assertEquals("legacy-data", target.readText())
    }

    @Test
    fun createsTargetParentDirectories() {
        val legacy = File(tempDir, "legacy.db").apply { writeText("data") }
        val target = File(tempDir, "nested/deep/target.db")

        migrateFileIfNeeded(legacyFile = legacy, targetFile = target)

        assertTrue(target.parentFile!!.exists())
        assertTrue(target.exists())
        assertFalse(legacy.exists())
    }
}
