package kz.divtech.odyssey.rotation.common.utils

import android.content.Context
import android.content.SharedPreferences
import kz.divtech.odyssey.rotation.BuildConfig
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.common.Config.AUTHORIZATION_VALUE_PREFIX
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Organization


object SharedPrefs {

    private const val USER_TOKEN = "user_token"
    private const val FIREBASE_TOKEN = "firebase_token"

    fun Context.isLoggedIn() = fetchToken(this).isNotEmpty()

    //Shared Preferences
    private fun Context.getSharedPrefs(): SharedPreferences {
        return this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    private fun Context.getSharedPrefsEditor(): SharedPreferences.Editor{
        return this.getSharedPrefs().edit()
    }

    //Authentication Token
    fun Context.saveAuthToken(token : String){
        this.getSharedPrefsEditor().putString(USER_TOKEN, token).apply()
    }

    private fun fetchToken(context: Context): String {
        return context.getSharedPrefs().getString(USER_TOKEN, "")!!
    }

    fun Context.getTokenWithBearer(): String {
        fetchToken(this).let {
            return "$AUTHORIZATION_VALUE_PREFIX $it"
        }
    }

    fun Context.clearAuthToken(){
        this.getSharedPrefsEditor().putString(USER_TOKEN, "").apply()
    }

    // DeviceId
    fun Context.saveDeviceId(userId: String){
        this.getSharedPrefsEditor().putString(Config.DEVICE_ID_KEY, userId).apply()
    }

    fun Context.fetchDeviceId(): String {
        return this.getSharedPrefs().getString(Config.DEVICE_ID_KEY,"")!!
    }

    //Firebase Token
    fun Context.saveFirebaseToken(token: String){
        this.getSharedPrefsEditor().putString(FIREBASE_TOKEN, token).apply()
    }

    fun Context.fetchFirebaseToken(): String{
        return this.getSharedPrefs().getString(FIREBASE_TOKEN, "")!!
    }

    //Host
    fun Context.saveUrl(url: String){
        this.getSharedPrefsEditor().putString("URL", url).apply()
    }

    fun Context.fetchUrl(): String{
        return this.getSharedPrefs().getString("URL", Config.PROXY_HOST)!!
    }

    fun Context.clearUrl(){
        this.getSharedPrefsEditor().putString("URL", Config.PROXY_HOST).apply()
    }

    fun Context.saveOrganizationName(organization: Organization){
        this.getSharedPrefsEditor().putString("organization", organization.name).apply()
    }

    fun Context.fetchOrganizationName(): String{
        return this.getSharedPrefs().getString("organization", "")!!
    }

    fun Context.saveAppLanguage(languageCode: String){
        this.getSharedPrefsEditor().putString("language", languageCode).apply()
    }

    fun Context.isAppHasLanguage() = this.fetchAppLanguage().isNotEmpty()

    fun Context.fetchAppLanguage(): String {
        return this.getSharedPrefs().getString("language", "")!!
    }

}