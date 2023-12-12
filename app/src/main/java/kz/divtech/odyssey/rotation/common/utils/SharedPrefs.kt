package kz.divtech.odyssey.rotation.common.utils

import android.content.Context
import android.content.SharedPreferences
import kz.divtech.odyssey.rotation.BuildConfig
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.common.Config.AUTHORIZATION_VALUE_PREFIX
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Organization


object SharedPrefs {

    private const val USER_TOKEN = "user_token"
    private const val FIREBASE_TOKEN = "firebase_token"

    fun isLoggedIn() = fetchToken().isNotEmpty()

    //Shared Preferences
    private fun getSharedPrefs(): SharedPreferences {
        return App.appContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    private fun getSharedPrefsEditor(): SharedPreferences.Editor{
        return getSharedPrefs().edit()
    }

    //Authentication Token
    fun saveAuthToken(token : String){
        getSharedPrefsEditor().putString(USER_TOKEN, token).apply()
    }

    private fun fetchToken(): String {
        return getSharedPrefs().getString(USER_TOKEN, "")!!
    }

    fun getTokenWithBearer(): String {
        fetchToken().let {
            return "$AUTHORIZATION_VALUE_PREFIX $it"
        }
    }

    fun clearAuthToken(){
        getSharedPrefsEditor().putString(USER_TOKEN, "").apply()
    }

    // DeviceId
    fun saveDeviceId(userId: String){
        getSharedPrefsEditor().putString(Config.DEVICE_ID_KEY, userId).apply()
    }

    fun fetchDeviceId(): String {
        return getSharedPrefs().getString(Config.DEVICE_ID_KEY,"")!!
    }

    //Firebase Token
    fun saveFirebaseToken(token: String){
        getSharedPrefsEditor().putString(FIREBASE_TOKEN, token).apply()
    }

    fun fetchFirebaseToken(): String{
        return getSharedPrefs().getString(FIREBASE_TOKEN, "")!!
    }

    //Host
    fun saveUrl(url: String){
        getSharedPrefsEditor().putString("URL", url).apply()
    }

    fun fetchUrl(): String{
        return getSharedPrefs().getString("URL", Config.PROXY_HOST)!!
    }

    fun clearUrl(){
        getSharedPrefsEditor().putString("URL", Config.PROXY_HOST).apply()
    }

    fun saveOrganizationName(organization: Organization){
        getSharedPrefsEditor().putString("organization", organization.name).apply()
    }

    fun fetchOrganizationName(): String{
        return getSharedPrefs().getString("organization", "")!!
    }

    fun saveAppLanguage(languageCode: String){
        getSharedPrefsEditor().putString("language", languageCode).apply()
    }

    fun isAppHasLanguage() = fetchAppLanguage().isNotEmpty()

    fun fetchAppLanguage(): String {
        return getSharedPrefs().getString("language", "")!!
    }

}