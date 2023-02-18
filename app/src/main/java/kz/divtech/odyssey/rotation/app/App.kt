package kz.divtech.odyssey.rotation.app

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.LoginRepository
import kz.divtech.odyssey.rotation.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import timber.log.Timber
import java.util.*

class App : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val findEmployeeRepository by lazy { FindEmployeeRepository() }
    val loginRepository by lazy { LoginRepository() }
    val orgInfoRepository by lazy { OrgInfoRepository(database.dao()) }
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