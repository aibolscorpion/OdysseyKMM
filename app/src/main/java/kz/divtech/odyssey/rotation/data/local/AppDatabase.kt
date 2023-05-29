package kz.divtech.odyssey.rotation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.ActiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.ArchiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip

@Database(entities = [ActiveTrip::class, ArchiveTrip::class, SingleTrip::class, Employee::class, Faq::class,
    Article::class, FullArticle::class, Notification::class, OrgInfo::class],
    version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun dao() : Dao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context, AppDatabase::class.java,
                    Config.DATABASE_NAME).build()
                INSTANCE = instance
                instance
            }
        }
    }

}