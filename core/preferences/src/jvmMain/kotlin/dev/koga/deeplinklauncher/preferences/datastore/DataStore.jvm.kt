package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File


internal fun dataStore(): DataStore<Preferences> =
    createDataStore(
        producePath = { dataStoreFileName },
    )
