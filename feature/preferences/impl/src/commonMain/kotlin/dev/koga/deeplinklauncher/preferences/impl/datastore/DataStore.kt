package dev.koga.deeplinklauncher.preferences.impl.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

internal fun createDataStore(
    path: () -> String,
) = PreferenceDataStoreFactory.createWithPath(
    produceFile = {
        path().toPath()
    },
)

internal const val dataStoreFileName = "user.preferences_pb"
