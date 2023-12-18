package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.article.FullArticleReponse

interface ArticleRepository {
    suspend fun getArticleById(id: Int): Resource<FullArticleReponse>
    suspend fun markArticleAsRead(id: Int): Resource<HttpResponse>
}