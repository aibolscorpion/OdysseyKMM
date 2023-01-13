package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@TypeConverters(ArticleListTypeConverter::class)
@Entity
data class News(
    val current_page: Int?,
    @PrimaryKey val data: List<Article>,
    val first_page_url: String?,
    val from: Int?,
    val last_page: Int?,
    val last_page_url: String?,
    val next_page_url: String?,
    val path: String?,
    val per_page: String?,
    val prev_page_url: String?,
    val to: Int?,
    val total: Int?
)

class ArticleListTypeConverter(){
    @TypeConverter
    fun fromString(value: String?): List<Article>{
        val listType: Type = object : TypeToken<List<Article>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(data : List<Article>) : String{
        return Gson().toJson(data)
    }
}
