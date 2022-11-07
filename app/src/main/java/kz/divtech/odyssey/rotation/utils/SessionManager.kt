package kz.divtech.odyssey.rotation.utils

import android.content.Context
import kz.divtech.odyssey.rotation.App
import kz.divtech.odyssey.rotation.BuildConfig
import kz.divtech.odyssey.rotation.Config.AUTHORIZATION_VALUE_PREFIX


class SessionManager {
    val context = App.appContext
    private val userToken = "user_token"
    private val sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    val isLoggedIn = fetchToken().isNotEmpty()

    fun saveAuthToken(token : String){
        editor.putString(userToken, token)
        editor.apply()
    }

    private fun fetchToken(): String{
        return sharedPref.getString(userToken, "")!!
    }

    fun getTokenWithBearer(): String {
        fetchToken().let {
            return "$AUTHORIZATION_VALUE_PREFIX $it"
        }
    }

    fun clearAuthToken(){
        editor.putString(userToken, "")
        editor.apply()
    }

}