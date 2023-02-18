package kz.divtech.odyssey.rotation.utils

import android.content.Context
import android.content.SharedPreferences
import kz.divtech.odyssey.rotation.BuildConfig
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Config.AUTHORIZATION_VALUE_PREFIX


object SharedPrefs {

    private const val USER_TOKEN = "user_token"
    private const val FIREBASE_TOKEN = "firebase_token"

    fun isLoggedIn(context: Context) = fetchToken(context).isNotEmpty()

    //
    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE)
    }

    private fun getSharedPrefsEditor(context: Context): SharedPreferences.Editor{
        return getSharedPrefs(context).edit()
    }

    //Authentication Token
    fun saveAuthToken(token : String, context: Context){
            getSharedPrefsEditor(context).putString(USER_TOKEN, token).apply()
    }

    private fun fetchToken(context: Context): String {
        return getSharedPrefs(context).getString(USER_TOKEN, "")!!
    }

    fun getTokenWithBearer(context: Context): String {
        fetchToken(context).let {
            return "$AUTHORIZATION_VALUE_PREFIX $it"
        }
    }

    fun clearAuthToken(context: Context){
        getSharedPrefsEditor(context).putString(USER_TOKEN, "").apply()
    }

    // DeviceId
    fun saveDeviceId(userId: String, context: Context){
        getSharedPrefsEditor(context).putString(Config.DEVICE_ID_KEY, userId).apply()
    }

    fun fetchDeviceId(context: Context): String {
        return getSharedPrefs(context).getString(Config.DEVICE_ID_KEY,"")!!
    }

    //Firebase Token
    fun saveFirebaseToken(token: String, context: Context){
        getSharedPrefsEditor(context).putString(FIREBASE_TOKEN, token).apply()
    }

    fun fetchFirebaseToken(context: Context): String{
        return getSharedPrefs(context).getString(FIREBASE_TOKEN, "")!!
    }

    //Host
    fun saveUrl(url: String, context: Context){
        getSharedPrefsEditor(context).putString("URL", url).apply()
    }

    fun fetchUrl(context: Context): String{
        return getSharedPrefs(context).getString("URL", Config.PROXY_HOST)!!
    }

    fun clearUrl(context: Context){
        getSharedPrefsEditor(context).putString("URL", Config.PROXY_HOST).apply()
    }

}