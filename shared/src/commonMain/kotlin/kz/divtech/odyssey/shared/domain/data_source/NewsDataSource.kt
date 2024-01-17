package kz.divtech.odyssey.shared.domain.data_source

import app.cash.paging.PagingSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

interface NewsDataSource {
    fun getNews(): PagingSource<Int, Article>
    suspend fun searchNews(searchQuery: String): List<Article>
    suspend fun insertNews(news: List<Article>)
    suspend fun refreshNews(news: List<Article>)
    suspend fun deleteNews()
}