package kz.divtech.odyssey.shared.data.local.data_source.full_article

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.FullArticleDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle

class SqlDelightFullArticleDataSource(database: OdysseyDatabase): FullArticleDataSource {
    private val queries = database.fullArticleQueries
    override suspend fun getArticleById(id: Int): FullArticle? {
        return queries.getArticleByid(id.toLong()).executeAsOneOrNull()?.toFullArticle()
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