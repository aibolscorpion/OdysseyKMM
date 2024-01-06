package kz.divtech.odyssey.shared.data.local.data_source.news

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NewsDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

class SqlDelightNewsDataSource(database: OdysseyDatabase): NewsDataSource {
    private val queries = database.newsQueries
    override suspend fun getNews(): List<Article> {
        return queries.getNewsPagingSource().executeAsList().toArticleList()
    }

    override suspend fun searchNews(searchQuery: String): List<Article> {
        return queries.searchByText(searchQuery).executeAsList().toArticleList()
    }

    override suspend fun refreshNews(news: List<Article>) {
        queries.transaction {
            queries.deleteNews()
            news.forEach { article ->
                val tags = Json.encodeToString(article.tags)
                queries.insertArticle(
                    id = article.id.toLong(),
                    title = article.title,
                    short_content = article.shortContent,
                    is_important = if(article.isImportant) 1L else 0L,
                    published_on = article.publishedOn,
                    tags = tags,
                    read_on = article.readOn
                )
            }
        }
    }

    override suspend fun deleteNews() {
        queries.deleteNews()
    }
}