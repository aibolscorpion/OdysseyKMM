package kz.divtech.odyssey.rotation.domain.model.trips

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@TypeConverters(DataConverter::class)
@Entity
data class Data(@PrimaryKey val data: Trips)

class DataConverter {
    @TypeConverter
    fun fromString(value: String?): Trips {
        val listType: Type = object : TypeToken<Trips>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(data: Trips): String {
        val gson = Gson()
        return gson.toJson(data)
    }
}