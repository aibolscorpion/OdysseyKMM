package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.News
import timber.log.Timber

class NewsRepository(private val dao: Dao) {

    val news: Flow<News> = dao.getNews()

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertNews(news: News){
        dao.insertNews(news)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNews(){
        dao.deleteNews()
    }

    suspend fun getNewsFromServer(){
        try{
            val response = RetrofitClient.getApiService().getArticles()
            when(response.code()){
                Constants.SUCCESS_CODE -> {
                    insertNews(response.body()!!)
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }

}