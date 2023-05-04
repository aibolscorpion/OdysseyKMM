package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(TagListConverter::class)
@Entity
data class Article(
    @PrimaryKey val id: Int,
    val title: String,
    val short_content: String,
    val is_important: Boolean,
    val published_on: String,
    val tags: List<Tag>,
    val read_on: String?
)