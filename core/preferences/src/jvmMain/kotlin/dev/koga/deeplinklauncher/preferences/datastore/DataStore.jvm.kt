package dev.koga.deeplinklauncher.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal fun dataStore(): DataStore<Preferences> =
    createDataStore(
        producePath = { dataStoreFileName },
    )
