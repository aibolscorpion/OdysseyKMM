package kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Pivot
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Tag
import java.lang.reflect.Type

@TypeConverters(PivotConverter::class, TagListConverter::class)
@Entity
data class FullArticle(
    val content: String?,
    val created_at: String?,
    val deleted_at: String?,
    val display: Boolean?,
    @PrimaryKey val id: Int?,
    val is_important: Int?,
    val mobile_publish_date: String?,
    val pivot: Pivot?,
    val published_on: String?,
    val published_user_id: String?,
    val short_content: String?,
    val short_title: String?,
    val tags: List<Tag>?,
    val title: String?,
    val type_id: Int?,
    val updated_at: String?,
    val user_id: Int?
)

class PivotConverter{
    @TypeConverter
    fun fromString(value: String): Pivot{
        val listType: Type = object : TypeToken<Pivot>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(pivot: Pivot): String{
        return Gson().toJson(pivot)
    }
}

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
