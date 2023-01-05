package kz.divtech.odyssey.rotation.domain.model.help.press_service.news

data class Article(
    val id: Int,
    val is_important: Int,
    val pivot: Pivot,
    val published_on: String,
    val read_on: String,
    val short_content: String,
    val tags: List<Tag>,
    val title: String
)