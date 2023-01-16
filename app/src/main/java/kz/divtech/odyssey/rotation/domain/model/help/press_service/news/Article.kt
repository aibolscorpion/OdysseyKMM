package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(TagListConverter::class)
@Entity
data class Article(
    @PrimaryKey val id: Int,
    val is_important: Int?,
    @Embedded val pivot: Pivot?,
    val published_on: String?,
    val read_on: String?,
    val short_content: String,
    val tags: List<Tag>,
    val title: String
)