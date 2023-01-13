package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import timber.log.Timber

class ArticleRepository(private val dao: Dao) {

    fun getArticleById(id: Int): Flow<FullArticle> {
        return dao.getArticleById(id)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertFullArticle(fullArticle: FullArticle){
        dao.insertFullArticle(fullArticle)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteFullArticles(){
        dao.deleteFullArticles()
    }

    suspend fun getArticleByIdFromServer(articleId: Int){
        try{
            val response = RetrofitClient.getApiService().getSpecificArticleById(articleId)
            when(response.code()){
                Constants.SUCCESS_CODE -> {
                    insertFullArticle(response.body()!!)
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }


    suspend fun markArticleAsRead(id: Int){
        try{
            RetrofitClient.getApiService().markAsReadArticleById(id)
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }

}