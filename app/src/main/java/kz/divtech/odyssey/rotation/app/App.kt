package kz.divtech.odyssey.rotation.app

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.FaqRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import timber.log.Timber
import java.util.*

class App : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val tripsRepository by lazy { TripsRepository(database.dao()) }
    val employeeRepository by lazy { EmployeeRepository(database.dao())}
    val faqRepository by lazy { FaqRepository(database.dao())}

    companion object {
        lateinit var appContext : Context
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)
        appContext = this

        val sharedPrefs = SharedPrefs()
        if(sharedPrefs.fetchDeviceId().isEmpty()) {
            val userId = UUID.randomUUID().toString()
            sharedPrefs.saveDeviceId(userId)
        }
    }

}