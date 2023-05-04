package kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Tag
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.TagListConverter

@TypeConverters(TagListConverter::class)
@Entity(tableName = "full_article")
data class FullArticle(
@PrimaryKey
    val id: Int,
    val title: String,
    val short_content: String,
    val is_important: Boolean,
    val published_on: String,
    val tags: List<Tag>,
    val read_on: String?,
    val content: String?
)

