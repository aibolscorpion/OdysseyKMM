package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticleResponse
import kz.divtech.odyssey.rotation.data.remote.result.*

class ArticleRepository(private val dao: Dao) {

    fun getArticleById(id: Int): Flow<FullArticle> {
        return dao.observeArticleById(id)
    }

    @WorkerThread
    suspend fun insertFullArticle(fullArticle: FullArticle){
        dao.insertFullArticle(fullArticle)
    }

    @WorkerThread
    suspend fun deleteFullArticles(){
        dao.deleteFullArticles()
    }

    suspend fun getArticleByIdFromServer(articleId: Int): Result<FullArticleResponse>{
        val response = RetrofitClient.getApiService().getArticleById(articleId)
        if(response.isSuccess()){
            val fullArticle = response.asSuccess().value.data
            insertFullArticle(fullArticle)
        }
        return response
    }


    suspend fun markArticleAsRead(id: Int){
        RetrofitClient.getApiService().markAsReadArticleById(id)
    }

}