package kz.divtech.odyssey.shared.domain.repository

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article

interface NewsRepository {
    fun getPagingNews(): Flow<PagingData<Article>>
    fun searchNewsFromDb(searchQuery: String): List<Article>
    fun deleteNews()

}