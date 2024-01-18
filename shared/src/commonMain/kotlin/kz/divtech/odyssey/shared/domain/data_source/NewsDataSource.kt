package kz.divtech.odyssey.shared.domain.data_source

import app.cash.paging.PagingSource
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

interface NewsDataSource {
    fun getNews(): PagingSource<Int, Article>
    fun searchNews(searchQuery: String): List<Article>
    fun insertNews(news: List<Article>)
    fun refreshNews(news: List<Article>)
    fun deleteNews()
}