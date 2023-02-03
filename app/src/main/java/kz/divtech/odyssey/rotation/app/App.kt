package kz.divtech.odyssey.rotation.app

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import timber.log.Timber
import java.util.*

class App : Application() {

    companion object {
        lateinit var appContext : Context
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)
        appContext = this

        if(SharedPrefs.fetchDeviceId(this).isEmpty()) {
            val userId = UUID.randomUUID().toString()
            SharedPrefs.saveDeviceId(userId, this)
        }
    }

}