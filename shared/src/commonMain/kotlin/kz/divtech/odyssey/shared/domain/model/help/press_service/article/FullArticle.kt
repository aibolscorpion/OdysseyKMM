package kz.divtech.odyssey.shared.domain.model.help.press_service.article

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullArticle(
    val id: Int,
    val title: String,
    @SerialName("short_content")
    val shortContent: String,
    @SerialName("is_important")
    val isImportant: Boolean,
    @SerialName("published_on")
    val publishedOn: String,
    val tags: List<Tag>,
    @SerialName("read_on")
    val readOn: String?,
    val content: String?
)
