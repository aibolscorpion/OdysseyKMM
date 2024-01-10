package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticleResponse

interface ArticleRepository {
    suspend fun getArticleById(id: Int): Resource<FullArticleResponse>
    suspend fun markArticleAsRead(id: Int): Resource<HttpResponse>
    suspend fun getArticleFromDbById(id: Int) : Flow<FullArticle?>
    suspend fun deleteFullArticles()
}