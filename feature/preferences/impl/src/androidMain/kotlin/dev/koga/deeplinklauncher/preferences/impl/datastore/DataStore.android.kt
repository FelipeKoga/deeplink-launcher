package dev.koga.deeplinklauncher.preferences.impl.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

internal fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        path = {
            File(context.filesDir, "datastore/$dataStoreFileName").path
        },
    )
