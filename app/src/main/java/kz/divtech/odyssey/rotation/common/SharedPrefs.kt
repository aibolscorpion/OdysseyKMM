package kz.divtech.odyssey.rotation.common

import android.content.Context
import android.content.SharedPreferences
import kz.divtech.odyssey.rotation.BuildConfig

object SharedPrefs {

    private const val FIREBASE_TOKEN = "firebase_token"

    //Shared Preferences
    private fun Context.getSharedPrefs(): SharedPreferences {
        return this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    //Firebase Token
    fun Context.saveFirebaseToken(token: String){
        this.getSharedPrefs().edit().putString(FIREBASE_TOKEN, token).apply()
    }

    fun Context.fetchFirebaseToken(): String{
        return this.getSharedPrefs().getString(FIREBASE_TOKEN, "")!!
    }

}