package kz.divtech.odyssey.rotation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kz.divtech.odyssey.rotation.common.Config
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
    version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun dao() : Dao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            return INSTANCE ?: synchronized(this){
                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE `Employee` ADD COLUMN `ua_confirmed` INTEGER NOT NULL DEFAULT 0")
                    }
                }
                val instance = Room.databaseBuilder(context, AppDatabase::class.java,
                    Config.DATABASE_NAME).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }

}