package kz.divtech.odyssey.shared.data.local.data_source.news

import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.Tag
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

fun List<database.Article>.toArticleList(): List<Article>{
    return map{
        val tagList: List<Tag> = Json.decodeFromString(it.tags)
        Article(
            id = it.id.toInt(),
            title = it.title,
            shortContent = it.short_content,
            isImportant = it.is_important == 1L,
            publishedOn =it. published_on,
            tags = tagList,
            readOn = it.read_on
        )
    }

}