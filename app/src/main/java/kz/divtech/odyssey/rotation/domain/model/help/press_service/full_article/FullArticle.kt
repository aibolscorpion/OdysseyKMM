package kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article

import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Pivot
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Tag

data class FullArticle(
    val content: String,
    val created_at: String,
    val deleted_at: Any,
    val display: Any,
    val id: Int,
    val is_important: Int,
    val mobile_publish_date: Any,
    val pivot: Pivot,
    val published_on: Any,
    val published_user_id: Any,
    val short_content: String,
    val short_title: Any,
    val tags: List<Tag>,
    val title: String,
    val type_id: Any,
    val updated_at: String,
    val user_id: Any
)