package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TagListConverter{
    @TypeConverter
    fun fromString(value: String): List<Tag>{
        val listType: Type = object : TypeToken<List<Tag>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(tagList: List<Tag>): String{
        return Gson().toJson(tagList)
    }
}
