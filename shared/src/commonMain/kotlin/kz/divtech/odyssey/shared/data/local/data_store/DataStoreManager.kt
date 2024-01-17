package kz.divtech.odyssey.shared.data.local.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kz.divtech.odyssey.shared.common.Config
import kz.divtech.odyssey.shared.common.Config.AUTHORIZATION_VALUE_PREFIX


class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    private val urlKey: Preferences.Key<String> = stringPreferencesKey("base_url")
    private val organizationKey: Preferences.Key<String> = stringPreferencesKey("organization")
    private val tokenKey: Preferences.Key<String> = stringPreferencesKey("token")
    private val deviceIdKey: Preferences.Key<String> = stringPreferencesKey("device_id")
    private val firebaseTokenKey: Preferences.Key<String> = stringPreferencesKey("firebase_token")
    private val languageKey: Preferences.Key<String> = stringPreferencesKey("language")

    //Url
    fun getBaseUrl(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[urlKey] ?: Config.PROXY_HOST
        }
    }

    suspend fun saveBaseUrl(url: String){
        dataStore.edit { preferences ->
            preferences[urlKey] = url
        }
    }

    suspend fun clearUrl(){
        dataStore.edit { preferences ->
            preferences[urlKey] = Config.PROXY_HOST
        }
    }

    //OrganizationName
    suspend fun saveOrganizationName(name: String){
        dataStore.edit { preferences ->
            preferences[organizationKey] = name
        }
    }

    fun getOrganizationName(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[organizationKey] ?: ""
        }
    }

    //AuthToken
    suspend fun saveAuthToken(token: String){
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    private fun getAuthToken(): Flow<String>{
       return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    suspend fun getTokenWithBearer(): String {
        return "$AUTHORIZATION_VALUE_PREFIX ${getAuthToken().first()}"
    }

    suspend fun isLoggedIn(): Boolean{
        return (getAuthToken().first()).isNotEmpty()
    }

    suspend fun clearAuthToken(){
        dataStore.edit { preferences ->
            preferences[tokenKey] = ""
        }
    }

    //DeviceId
    fun getDeviceId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[deviceIdKey] ?: ""
        }
    }

    suspend fun saveDeviceId(deviceId: String){
        dataStore.edit { preferences ->
            preferences[deviceIdKey] = deviceId
        }
    }

    //FirebaseToken
    fun getFirebaseToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[firebaseTokenKey] ?: ""
        }
    }

    suspend fun saveFirebaseToken(firebaseToken: String){
        dataStore.edit { preferences ->
            preferences[firebaseTokenKey] = firebaseToken
        }
    }

    //Language
    suspend fun saveLanguage(language: String){
        dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    fun getLanguage(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[languageKey] ?: ""
        }
    }

    suspend fun isAppHasLanguage(): Boolean{
        return (getLanguage().first()).isNotEmpty()
    }


}