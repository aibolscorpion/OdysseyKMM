package kz.divtech.odyssey.shared.data.local.data_source.full_article

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.FullArticleDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.Tag

class SqlDelightFullArticleDataSource(database: OdysseyDatabase): FullArticleDataSource {
    private val queries = database.fullArticleQueries
    override suspend fun getArticleById(articleId: Int): Flow<FullArticle?> {
        return queries.getArticleByid(id = articleId.toLong(),
            mapper = { id, title, short_content, is_important, published_on, tags, read_on, content ->
            val tagList: List<Tag> = Json.decodeFromString(tags)
            FullArticle(
                id = id.toInt(),
                title = title,
                shortContent = short_content,
                isImportant = is_important == 1L,
                publishedOn = published_on,
                tags = tagList,
                readOn = read_on,
                content = content
            )
        }).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override suspend fun insertArticle(fullArticle: FullArticle) {
        queries.insertArticle(
            id = fullArticle.id.toLong(),
            title = fullArticle.title,
            short_content = fullArticle.shortContent,
            is_important = if(fullArticle.isImportant) 1L else 0L,
            published_on = fullArticle.publishedOn,
            read_on = fullArticle.readOn,
            tags = Json.encodeToString(fullArticle.tags),
            content = fullArticle.content
        )
    }

    override suspend fun deleteFullArticles() {
        queries.deleteFullArticles()
    }
}