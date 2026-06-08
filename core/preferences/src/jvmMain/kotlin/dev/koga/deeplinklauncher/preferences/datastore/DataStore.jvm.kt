package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.koga.deeplinklauncher.platform.JvmAppDataDirectory
import dev.koga.deeplinklauncher.platform.migrateFileIfNeeded
import java.io.File

internal fun dataStore(): DataStore<Preferences> {
    val appDir = JvmAppDataDirectory.resolve()
    val target = File(appDir, "datastore/$dataStoreFileName")
    val legacy = File("datastore/$dataStoreFileName")

    migrateFileIfNeeded(legacyFile = legacy, targetFile = target)

    return createDataStore(
        path = { target.path },
    )
}
