package kz.divtech.odyssey.rotation.utils

import android.content.Context
import kz.divtech.odyssey.rotation.BuildConfig
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Config.AUTHORIZATION_VALUE_PREFIX


class SharedPrefs() {
    val context = App.appContext
    private val userToken = "user_token"
    private val sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    val isLoggedIn = fetchToken().isNotEmpty()
    fun saveAuthToken(token : String){
        editor.putString(userToken, token)
        editor.apply()
    }

    private fun fetchToken() = sharedPref.getString(userToken, "")!!

    fun getTokenWithBearer(): String {
        fetchToken().let {
            return "$AUTHORIZATION_VALUE_PREFIX $it"
        }
    }

    fun clearAuthToken(){
        editor.putString(userToken, "")
        editor.apply()
    }

    fun  saveDeviceId(userId: String){
        editor.putString(Config.DEVICE_ID_KEY, userId)
        editor.apply()
    }

    fun fetchDeviceId() = sharedPref.getString(Config.DEVICE_ID_KEY,"")!!

}