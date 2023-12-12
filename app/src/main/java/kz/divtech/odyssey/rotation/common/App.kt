package kz.divtech.odyssey.rotation.common

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.fetchDeviceId
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.saveDeviceId
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class App : Application() {
    companion object { lateinit var appContext : Context }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)
        appContext = this

        if(fetchDeviceId().isEmpty()) {
            val userId = UUID.randomUUID().toString()
            saveDeviceId(userId)
        }
    }

}