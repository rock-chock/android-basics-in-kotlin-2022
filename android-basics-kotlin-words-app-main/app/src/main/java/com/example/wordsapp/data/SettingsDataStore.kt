package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using preferencesDataStore delegate, with the Context as receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class SettingsDataStore(context: Context) {

    // Store a boolean value that specifies whether the user setting is a linear layout
    private val IS_LINEAR_LAYOUT_MANAGER =  booleanPreferencesKey("is_linear_layout_manager")

    // Suspend function that updates values in Preferences DataStore
    // Should be executed outside of the main thread
    suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean, context: Context) {
        // edit() suspend function transactionally updates the data in DataStore.
        // All of the code in the transform block is treated as a single transaction and is moved to Dispatcher.IO.
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }

    // Preferences DataStore exposes the data stored in a Flow<Preferences> that emits every time a preference has changed.
    // We want to expose only the Boolean value from the Flow<Preferences> object.
    // Expose a preferenceFlow: Flow<UserPreferences>, constructed based on dataStore.data: Flow<Preferences>,
    // map it to retrieve the Boolean preference.
    // Since the Datastore is empty on the first run, return true by default.
    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        // Catch IOExceptions that may occur when accessing the data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        // Map the dataStore's Flow<Preferences> and get the Boolean value
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true
        }

}





