package kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Pivot
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Tag
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.TagListConverter

@TypeConverters(TagListConverter::class)
@Entity(tableName = "full_article")
data class FullArticle(
    val content: String?,
    val created_at: String?,
    val deleted_at: String?,
    val display: Boolean?,
    @PrimaryKey val id: Int?,
    val is_important: Int?,
    val mobile_publish_date: String?,
    @Embedded val pivot: Pivot?,
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

