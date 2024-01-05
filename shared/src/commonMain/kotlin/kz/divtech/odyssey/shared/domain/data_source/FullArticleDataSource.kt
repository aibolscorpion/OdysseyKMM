package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle

interface FullArticleDataSource {
    suspend fun getArticleById(id: Int) : FullArticle?
    suspend fun insertArticle(fullArticle: FullArticle)
    suspend fun deleteFullArticles()
}