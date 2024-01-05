package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticleReponse

interface ArticleRepository {
    suspend fun getArticleById(id: Int): Resource<FullArticleReponse>
    suspend fun markArticleAsRead(id: Int): Resource<HttpResponse>
    suspend fun getArticleFromDbById(id: Int) : FullArticle?
    suspend fun deleteFullArticles()
}