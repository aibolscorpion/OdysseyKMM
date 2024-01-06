package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

interface NewsDataSource {
    suspend fun getNews(): List<Article>
    suspend fun searchNews(searchQuery: String): List<Article>
    suspend fun refreshNews(news: List<Article>)
    suspend fun deleteNews()
}