package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.koga.deeplinklauncher.preferences.datastore.createDataStore
import dev.koga.deeplinklauncher.preferences.datastore.dataStoreFileName
import java.io.File

internal fun dataStore(): DataStore<Preferences> =
    createDataStore(
        path = {
            File("datastore/$dataStoreFileName").path
        },
    )
