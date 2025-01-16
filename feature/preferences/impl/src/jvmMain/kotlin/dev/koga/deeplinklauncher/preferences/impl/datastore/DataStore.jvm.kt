package dev.koga.deeplinklauncher.preferences.impl.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.koga.deeplinklauncher.preferences.impl.datastore.createDataStore
import dev.koga.deeplinklauncher.preferences.impl.datastore.dataStoreFileName
import java.io.File

internal fun dataStore(): DataStore<Preferences> =
    createDataStore(
        path = {
            File("datastore/$dataStoreFileName").path
        },
    )
