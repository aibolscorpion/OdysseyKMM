package kz.divtech.odyssey.shared.data.local.data_source.full_article

import database.Full_article
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.Tag

fun Full_article.toFullArticle(): FullArticle{
    val tagList: List<Tag> = Json.decodeFromString(tags)
    return FullArticle(
        id = id.toInt(),
        title = title,
        shortContent = short_content,
        isImportant = is_important == 1L,
        publishedOn = published_on,
        tags = tagList,
        readOn = read_on,
        content = content
    )
}