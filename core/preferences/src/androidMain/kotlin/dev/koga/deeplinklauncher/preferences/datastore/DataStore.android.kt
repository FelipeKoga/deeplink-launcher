package dev.koga.deeplinklauncher.preferences.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module
import java.io.File


internal fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = {
            File(context.filesDir, "datastore/$dataStoreFileName").path
        }
    )