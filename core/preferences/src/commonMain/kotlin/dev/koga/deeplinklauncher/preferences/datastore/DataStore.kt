package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal fun createDataStore(
    producePath: () -> String,
): DataStore<Preferences> = createDataStore(
    path = { producePath() },
)

internal fun createDataStore(
    context: Any? = null,
    path: (context: Any?) -> String,
) = PreferenceDataStoreFactory.createWithPath(
    produceFile = {
        path(context).toPath()
    },
)

internal const val dataStoreFileName = "user.preferences_pb"
