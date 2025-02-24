package daimao.nekomimi.notifycalender

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val ALLOW_POST_KEY = booleanPreferencesKey("allow_post")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun saveAllowPostState(context: Context, allow: Boolean) {
    context.dataStore.edit { settings ->
        settings[ALLOW_POST_KEY] = allow
    }
}

fun loadAllowPostState(context: Context): Flow<Boolean> {
    return context.dataStore.data.map { p -> p[ALLOW_POST_KEY] ?: false }
}



