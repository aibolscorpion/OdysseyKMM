package kz.divtech.odyssey.shared.data.local.data_source.news

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.NewsDataSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.Tag
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

class SqlDelightNewsDataSource(database: OdysseyDatabase): NewsDataSource {
    private val queries = database.newsQueries
    override fun getNews(): PagingSource<Int, Article> {
        return QueryPagingSource(
            countQuery = queries.countNews(),
            transacter = queries,
            context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                queries.getNewsPagingSource(limit, offset, mapper = { id, title, short_content, is_important, published_on, tags, read_on ->
                    val tagList: List<Tag> = Json.decodeFromString(tags)
                    Article(
                        id = id.toInt(),
                        title = title,
                        shortContent = short_content,
                        isImportant = (is_important == 1L),
                        publishedOn = published_on,
                        tags = tagList,
                        readOn = read_on
                    )
                })
            }
        )
    }

    override suspend fun searchNews(searchQuery: String): List<Article> {
        return queries.searchByText(searchQuery).executeAsList().toArticleList()
    }

    override suspend fun insertNews(news: List<Article>) {
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