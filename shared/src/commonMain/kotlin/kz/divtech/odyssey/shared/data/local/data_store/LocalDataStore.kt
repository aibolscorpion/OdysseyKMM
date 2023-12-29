package kz.divtech.odyssey.shared.data.local.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File


object LocalDataStore {
    fun createDataStore(
        producePath: () -> File
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { producePath() },
    )

     const val dataStoreFileName = "odyssey.preferences_pb"
}
