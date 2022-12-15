package kz.divtech.odyssey.rotation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data

@Database(entities = [Data::class, Employee::class], version = 1, exportSchema = false)

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