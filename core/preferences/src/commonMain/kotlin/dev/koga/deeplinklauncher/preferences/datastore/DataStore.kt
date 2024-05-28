package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath


expect fun dataStorePreferences(
    context: Any? = null,
): DataStore<Preferences>

internal fun createDataStore(
    context: Any? = null,
    path: (context: Any?) -> String,
) = PreferenceDataStoreFactory.createWithPath(
    produceFile = {
        path(context).toPath()
    }
)

internal const val dataStoreFileName = "user.preferences_pb"
