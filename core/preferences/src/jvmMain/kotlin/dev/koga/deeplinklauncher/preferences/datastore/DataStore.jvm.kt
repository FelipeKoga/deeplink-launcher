package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun dataStorePreferences(
    context: Any?
): DataStore<Preferences> = createDataStore(
    path = {
        dataStoreFileName
    }
)